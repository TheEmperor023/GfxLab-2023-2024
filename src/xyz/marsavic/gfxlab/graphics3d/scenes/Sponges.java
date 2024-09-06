package xyz.marsavic.gfxlab.graphics3d.scenes;

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

public class Sponges extends Scene.Base {
    public Sponges(){
        SDF sponge = SDF.sponge(SDF.box(Vec3.xyz(1,1,1)), 3);
        SDF ballSponge = SDF.sponge(SDF.ball(Vec3.xyz(0,0,0), 1), 3);

        Collection<Solid> solids = new ArrayList<>();
        Collections.addAll(solids,
                ballSponge.transformed(Affine.IDENTITY.then(Affine.scaling(0.5)).then(Affine.rotationAboutY(0.125)).then(Affine.translation(Vec3.xyz(0,0.5,0)))),
                sponge.transformed(Affine.IDENTITY.then(Affine.scaling(0.3)).then(Affine.translation(Vec3.xyz(0,-0.7,0))).then(Affine.rotationAboutY(0.125)).then(Affine.rotationAboutX(0.125)))
        );

        solid = Group.of(solids);

        Collections.addAll(lights,
                Light.p(Vec3.xyz(0.7,0.7,-1)),
                Light.p(Vec3.xyz(0.7,-0.7,-1)),
                Light.p(Vec3.xyz(-0.7,-0.7,-1)),
                Light.p(Vec3.xyz(-0.7,0.7,-1)),
                Light.p(Vec3.xyz(0,0,-1))
        );
    }
}
