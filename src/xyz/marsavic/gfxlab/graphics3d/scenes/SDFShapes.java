package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Affine;
import xyz.marsavic.gfxlab.graphics3d.Light;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.Solid;
import xyz.marsavic.gfxlab.graphics3d.solids.Group;
import xyz.marsavic.gfxlab.graphics3d.solids.SDF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SDFShapes extends Scene.Base {
    public SDFShapes(){
        SDF cross = SDF.CenterCross();
        SDF union = SDF.smoothUnion(SDF.ball(Vec3.xyz(-0.85,0.1, 0),0.2), SDF.ball(Vec3.xyz(-0.4,0, 0),0.2), 0.3);
        SDF box = SDF.box(Vec3.xyz(0.3,0.4,0.3));
        Collection<Solid> solids = new ArrayList<>();
        Collections.addAll(solids,
                    cross.transformed(Affine.IDENTITY.then(Affine.scaling(0.2).then(Affine.rotationAboutY(0.4)).then(Affine.translation(Vec3.xyz(0,-0.8,0))))),
                    union,
                    box.transformed(Affine.IDENTITY.then(Affine.translation(Vec3.xyz(1.3,0,1.3))))
                );

        solid = Group.of(solids);

        Collections.addAll(lights,
                Light.p(Vec3.xyz(0.7,0.7,0)),
                Light.p(Vec3.xyz(0.7,-0,0)),
                Light.p(Vec3.xyz(-0.7,-0,0)),
                Light.p(Vec3.xyz(-0.7,0.7,0)),
                Light.p(Vec3.xyz(0,0,-1))
        );
    }

}
