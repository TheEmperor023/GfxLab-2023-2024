package xyz.marsavic.gfxlab.graphics3d.solids;

import xyz.marsavic.functions.F1;
import xyz.marsavic.geometry.Vec;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.Solid;
import xyz.marsavic.random.RNG;
import xyz.marsavic.utils.Numeric;

import java.util.Random;

public interface SDF extends Solid {

	double dist(Vec3 p);


	
	double DELTA_HIT = 1e-4;
	int MAX_STEPS = 32;

	
	@Override
	default Hit firstHit(Ray ray, double afterTime) {
		Ray ray_ = ray.normalized_();
		double l = ray.d().length();
		double t = 0;
		int nSteps = 0;
	
		while (true) {
			Vec3 p = ray_.at(t);
			double d = dist(p);
			if (d < DELTA_HIT) {
				return new SDFHit(t / l, this, p);
			}
			t += d;
			nSteps++;
			if (nSteps >= MAX_STEPS) {
				return (d < 1) ? new SDFHit(t / l, this, p) : Hit.AtInfinity.axisAlignedGoingIn(ray_.d());
			}
		}
	}

	
	class SDFHit implements Hit {
		
		
		private final double t;
		private final SDF sdf;
		private final Vec3 p;
		
		SDFHit(double t, SDF sdf, Vec3 p) {
			this.t = t;
			this.sdf = sdf;
			this.p = p;
		}
		
		@Override
		public double t() {
			return t;
		}
		
		static final double EPS = 0.0001;
		static final Vec3 dx = Vec3.EX.mul(EPS);
		static final Vec3 dy = Vec3.EY.mul(EPS);
		static final Vec3 dz = Vec3.EZ.mul(EPS);
		
		@Override
		public Vec3 n() {
			double d = sdf.dist(p);
			return Vec3.xyz(
					(sdf.dist(p.add(dx)) - d) / EPS,
					(sdf.dist(p.add(dy)) - d) / EPS,
					(sdf.dist(p.add(dz)) - d) / EPS
			);
		}
		
		@Override
		public Material material() {
			return Material.matte(Color.hsb(0.31, 1, 1)).specular(Color.WHITE).shininess(62);
		}
		
		@Override
		public Vector uv() {
			return Vector.ZERO;
		}
	}
	
	
	
	static SDF ball(Vec3 c, double r) { return p -> p.sub(c).length() - r; }
	static SDF box(Vec3 r) { return p -> Vec3.max(p.abs().sub(r), Vec3.ZERO).length() - Math.max(0, r.sub(p.abs()).min()); }
	static SDF union(SDF a, SDF b) { return p -> Math.min(a.dist(p), b.dist(p)); }
	static SDF extend(SDF sdf, double r) { return p -> sdf.dist(p) - r; }
	static SDF smoothUnion(SDF a, SDF b, double r) {
		return p -> {
			double da = a.dist(p);
			double db = b.dist(p);
			double k = Numeric.clamp((db - da) / r * 0.5 + 0.5);
			return da * k + db * (1 - k) - k * (1 - k) * r;
		};
	}
	static SDF suptraction(SDF a, SDF b) {return p -> Math.max(a.dist(p) * -1, b.dist(p));}
	static SDF torus(Vec3 t){
		return p -> {
			Vector q = Vector.xy(Vector.xy(p.x(),p.z()).length() - t.x(), p.y());
			return q.length() - t.y();
		};
	}

	static SDF cylinder(Vec3 c, double h, double r){
		return p -> {
			Vector d = Vector.xy(Vector.xy(c.x(), c.z()).length(), p.y()).abs().sub(Vector.xy(r,h));
			return Math.min(Math.max(d.x(),d.x()), 0.0) + Math.max(d.length(), 0.0);
		};
	}

	static SDF sdPlane(Vec3 n, double h ){
		return p -> {
			return p.dot(n) - h;
		};
	}

	static SDF CenterCross(){
		SDF cross = union(union(box(Vec3.xyz(Double.POSITIVE_INFINITY,1,1)), box(Vec3.xyz(1, Double.POSITIVE_INFINITY,1))), box(Vec3.xyz(1,1,Double.POSITIVE_INFINITY)));
		return p -> {
			return cross.dist(p);
		};
	}



	static SDF sponge(SDF sdf,int iter){return p -> {
		double d = sdf.dist(p);

		double s = 1.0;
		for( int m = 0; m < iter ; m++ )
		{
			Vec3 a = Vec3.xyz(Numeric.mod((p.x() * s), 2.0) - 1.0,Numeric.mod((p.y() * s), 2.0) - 1.0,Numeric.mod((p.z() * s), 2.0) - 1.0);
			s *= 3.0;
			Vec3 w = Vec3.xyz(Math.abs(1.0 - 3 * Math.abs(a.x())),Math.abs(1.0 - 3 * Math.abs(a.y())),Math.abs(1.0 - 3 * Math.abs(a.z())));

			double da = Math.max(w.x(),w.y());
			double db = Math.max(w.y(),w.z());
			double dc = Math.max(w.z(),w.x());
			double c = (Math.min(da, Math.min(db,dc))-1.0) / s;

			d = Math.max(d,c);
		}

		return d;
	};
	}

}
