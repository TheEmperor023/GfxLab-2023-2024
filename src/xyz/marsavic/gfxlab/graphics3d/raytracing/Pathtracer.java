package xyz.marsavic.gfxlab.graphics3d.raytracing;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.random.sampling.Sampler;
import xyz.marsavic.utils.Hashing;


public class Pathtracer extends Raytracer {
	
	private static final double EPSILON = 1e-9;
	private static final long seed = 0x68EFD508E309A865L;
	
	private final int maxDepth;
	
	
	public Pathtracer(Scene scene, Camera camera, int maxDepth) {
		super(scene, camera);
		this.maxDepth = maxDepth;
	}
	
	public Pathtracer(Scene scene, Camera camera) {
		this(scene, camera, 16);
	}
	
	
	@Override
	protected Color sample(Ray ray) {
		return radiance(ray, maxDepth, new Sampler(Hashing.mix(seed, ray)));
	}
	
	
	private Color radiance(Ray ray, int depthRemaining, Sampler sampler) {
		if (depthRemaining <= 0) return Color.BLACK;
		
		Hit hit = scene.solid().firstHit(ray, EPSILON);
		if (hit.t() == Double.POSITIVE_INFINITY) {
			return scene.colorBackground();
		}
		
		Material material = hit.material();
		Color result = material.emittance();
		
		Vec3 i = ray.d().inverse();                 // Incoming direction
		Vec3 n_ = hit.n_();                         // Normalized normal to the body surface at the hit point
		BSDF.Result bsdfResult = material.bsdf().sample(sampler, n_, i);
		
		if (bsdfResult.color().notZero()) {
			Vec3 p = ray.at(hit.t());               // Point of collision
			Ray rayScattered = Ray.pd(p, bsdfResult.out());
			Color rO = radiance(rayScattered, depthRemaining - 1, sampler);
			Color rI = rO.mul(bsdfResult.color());
			result = result.add(rI);
		}

		return result;
		//return addFog(result, ray.p().length(), ray.at(0), ray.d(),0.1,0.01);
	}

	private Color addFog(Color inColor, double t, Vec3 o, Vec3 d, double a, double b){
		double fogAmount = ((a/b) * Math.pow(Math.E, -1 * o.y() * b) * (1.0 - Math.pow(Math.E, -t * d.y() * b))/d.y());
		Color fogColor = Color.WHITE;
		return inColor.add(fogColor.mul(fogAmount));
	}
}
