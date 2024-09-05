package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.functions.F1;
import xyz.marsavic.geometry.Vec;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.solids.Group;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;
import xyz.marsavic.gfxlab.graphics3d.solids.SDF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static xyz.marsavic.gfxlab.graphics3d.solids.SDF.*;

public class TestSDF extends Scene.Base {
	
	public TestSDF() {
		F1<Material, Vector> grid  = v -> Material.matte(                     v.add(Vector.xy(0.005)).mod(0.25).min() < 0.01 ? 0.5 : 1);
		F1<Material, Vector> gridR = v -> Material.matte(Color.hsb(0   , 0.5, v.add(Vector.xy(0.005)).mod(0.25).min() < 0.01 ? 0.5 : 1));
		F1<Material, Vector> gridG = v -> Material.matte(Color.hsb(0.33, 0.5, v.add(Vector.xy(0.005)).mod(0.25).min() < 0.01 ? 0.5 : 1));
		
//		SDF s = smoothUnion(
//					ball(Vec3.xyz(-0.4, 0, 0), 0.3),
//					box(Vec3.EXYZ.mul(0.4)),
//					0.2
//				);
		SDF s = SDF.sdPlane(Vec3.xyz( 0,  1,  0), -0.8);
		SDF w2 = SDF.sdPlane(Vec3.xyz(1,0,0), -1);
		SDF w3 = SDF.sdPlane(Vec3.xyz(0,-1,0), -1);
		SDF w4 = SDF.sdPlane(Vec3.xyz(-1,0,0), -1);
		SDF w5 = SDF.sdPlane(Vec3.xyz(0, 0, -1) ,-1);
		SDF b = SDF.ball(Vec3.xyz(0,0,0), 0.5);
		SDF box = SDF.box(Vec3.xyz(0.5,0.5,0.5));
		SDF cross = union(union(box(Vec3.xyz(Double.POSITIVE_INFINITY,0.1,0.1)), box(Vec3.xyz(0.1, Double.POSITIVE_INFINITY,0.1))), box(Vec3.xyz(0.1,0.1,Double.POSITIVE_INFINITY)));
		SDF sponge = SDF.sponge(SDF.box(Vec3.xyz(1,1,1)),2);

		Collection<Solid> solids = new ArrayList<>();
		Collections.addAll(solids,
//			HalfSpace.pn(Vec3.xyz(-1,  0,  0), Vec3.xyz( 1,  0,  0), gridG),
//			HalfSpace.pn(Vec3.xyz( 1,  0,  0), Vec3.xyz(-1,  0,  0), gridR),
//			HalfSpace.pn(Vec3.xyz( 0, -1,  0), Vec3.xyz( 0,  1,  0), grid),
//			HalfSpace.pn(Vec3.xyz( 0,  1,  0), Vec3.xyz( 0, -1,  0), grid),
//			HalfSpace.pn(Vec3.xyz( 0,  0,  1), Vec3.xyz( 0,  0, -1), grid),
//			sponge.transformed(Affine.IDENTITY.then(Affine.scaling(0.7)).then(Affine.rotationAboutY(0.2)))
				sponge.transformed(Affine.IDENTITY.then(Affine.scaling(0.5))),
				w4,
				w5,
				w3,
				s
		);
		
		solid = Group.of(solids);
		
		Collections.addAll(lights,
//			Light.p(Vec3.xyz( 0.9,  0.9  ,-4)),
//			Light.p(Vec3.xyz(-0.9,  0.9,  -4)),
//			Light.p(Vec3.xyz( -0.9,  -0.5, -4)),
//			Light.p(Vec3.xyz(0.9,  -0.9, -4)),
//			Light.p(Vec3.xyz(0,0.3,-3)),
//				Light.p(Vec3.xyz(-2,0,0))
				Light.p(Vec3.xyz(0.7,-0.3, -1)),
				Light.p(Vec3.xyz(-1,-0.3, -1)),
				Light.p(Vec3.xyz(0,0,0))
		);
	}
}
