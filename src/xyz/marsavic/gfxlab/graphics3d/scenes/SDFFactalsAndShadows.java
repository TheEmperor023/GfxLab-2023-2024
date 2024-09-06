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

public class SDFFactalsAndShadows extends Scene.Base {
    public SDFFactalsAndShadows(){
        SDF ground = SDF.sdPlane(Vec3.xyz(0,1,0), -1);
        SDF sponge = SDF.sponge(SDF.box(Vec3.xyz(1,1,1)), 4);

        Collection<Solid> solids = new ArrayList<>();
        Collections.addAll(solids,
                ground,
                sponge.transformed(Affine.IDENTITY.then(Affine.scaling(0.5)).then(Affine.rotationAboutY(0.2)).then(Affine.rotationAboutX(0.2)))
                );

        solid = Group.of(solids);

        Collections.addAll(lights,
                Light.p(Vec3.xyz(0.2,1,0)),
                Light.p(Vec3.xyz(0.6,1,0)),
                Light.p(Vec3.xyz(-0.6,1,0)),
                Light.p(Vec3.xyz(0,0,-1))
                );
    }
}
