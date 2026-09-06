package K.content.effects;

import K.content.Fx.OtherFx;
import K.content.extend.util.Utils;
import K.entities.DrawEntity;
import K.entities.RenderGroupEntity;
import arc.*;
import arc.audio.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import arc.util.pooling.Pool.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static arc.math.geom.Intersector.*;
import static arc.math.geom.Geometry.*;
import static mindustry.Vars.*;

/**
 * @author EyeOfDarkness
 */
public class Severation extends DrawEntity implements QuadTreeObject{
    static FloatSeq intersections = new FloatSeq(), side1 = new FloatSeq(), side2 = new FloatSeq();
    static Seq<CutTri> returnTri = new Seq<>(), tmpTris = new Seq<>(), tmpTris2 = new Seq<>();
    static Seq<Severation> tmpCuts = new Seq<>();
    static Vec2 tmpVec = new Vec2(), tmpVec2 = new Vec2(), tmpVec3 = new Vec2();
    static float[] tmpVerts = new float[24];
    static float minArea = 4f * 4f;

    static int slashIDs = 0;
    static QuadTree<Severation> cutTree;
    public static Seq<Severation> cutsSeq = new Seq<>();
    static Seq<Slash> slashes = new Seq<>();
    static Pool<Slash> slashPool = new Utils.BasicPool<>(Slash::new);

    public Seq<CutTri> tris = new Seq<>();
    public float bounds = 0f;
    public float area = 0f;
    float centerX, centerY;
    public float rotation;
    public float width;
    public float height;
    public TextureRegion region = new TextureRegion();
    IntSet collided = new IntSet();

    public float color = Color.whiteFloatBits;
    public float z = Layer.flyingUnit, shadowZ, zTime;
    public Effect explosionEffect = OtherFx.FlameFX.fragmentExplosion;
    public Sound explosionSound = Sounds.none;

    float time = 0f;
    public float lifetime = 3f * 60f;
    public float vx, vy, vr;
    public float drag = 0.05f;
    public boolean wasCut = false;

    public static void init(){
        Events.on(ResetEvent.class, e -> {
            slashes.clear();
            cutsSeq.clear();
        });
        Events.on(EventType.WorldLoadEvent.class, e -> cutTree = new QuadTree<>(new Rect(-finalWorldBounds, -finalWorldBounds, world.width() * tilesize + finalWorldBounds * 2, world.height() * tilesize + finalWorldBounds * 2)));
    }
    public static void updateStatic(){
        if(state.isGame()){
            if(cutTree != null){
                cutTree.clear();
                for(Severation cuts : cutsSeq){
                    cutTree.insert(cuts);
                }
            }
            if(!slashes.isEmpty()){
                slashes.removeAll(s -> {
                    Utils.intersectLine(cutTree, 1f, s.x1, s.y1, s.x2, s.y2, (c, x, y) -> {
                        Rect b = Tmp.r3;
                        c.hitbox(b);
                        if(!b.contains(s.x1, s.y1) && !b.contains(s.x2, s.y2) && c.collided.add(s.id)){
                            //float len = Mathf.random(0.8f, 1.2f);
                            c.cutWorld(s.x1, s.y1, s.x2, s.y2, cc -> {
                                Vec2 n = nearestSegmentPoint(s.x1, s.y1, s.x2, s.y2, cc.x, cc.y, tmpVec3);
                                int side = pointLineSide(s.x1, s.y1, s.x2, s.y2, cc.x, cc.y);
                                //float len = Mathf.rand
                                //n.sub(cc.x, cc.y).nor().scl(-1.5f * len);
                                float dst = n.dst(cc.x, cc.y);
                                n.sub(cc.x, cc.y).scl((-2.5f) / 20f).limit(3f);
                                cc.vx /= 1.5f;
                                cc.vy /= 1.5f;
                                cc.vr /= 1.5f;
                                cc.vx += n.x;
                                cc.vy += n.y;
                                //cc.vr += -2f * side;
                                cc.vr += (-5f * side) / (1f + dst / 5f);
                            });
                        }
                    });
                    s.time += Time.delta;
                    if(s.time >= 4f) slashPool.free(s);
                    return s.time >= 4f;
                });
            }
        }
    }
    public static void slash(float x1, float y1, float x2, float y2){
        Slash s = slashPool.obtain();
        s.x1 = x1;
        s.y1 = y1;
        s.x2 = x2;
        s.y2 = y2;
        slashes.add(s);
    }

    public static Severation generate(TextureRegion region, float x, float y, float width, float height, float rotation){
        Severation c = new Severation();
        c.region.set(region);
        //c.region = region;
        c.width = width;
        c.height = height;
        c.rotation = rotation;
        c.x = x;
        c.y = y;

        for(int i = 0; i < 2; i++){
            CutTri tr = new CutTri();
            float[] p = tr.pos;
            p[0] = i == 0 ? 0f : 1f;
            p[1] = i == 0 ? 0f : 1f;
            p[2] = 1f;
            p[3] = 0f;
            p[4] = 0f;
            p[5] = 1f;

            c.tris.add(tr);
        }

        c.updateBounds();

        c.add();
        return c;
    }

    public void cutWorld(float x1, float y1, float x2, float y2, Cons<Severation> force){
        if(!added || area < minArea) return;

        if(force == null){
            float fx1 = x1, fy1 = y1, fx2 = x2, fy2 = y2;
            force = cc -> {
                Vec2 n = nearestSegmentPoint(fx1, fy1, fx2, fy2, cc.x, cc.y, tmpVec2);
                int side = pointLineSide(fx1, fy1, fx2, fy2, cc.x, cc.y);
                //float len = Mathf.rand
                //n.sub(cc.x, cc.y).nor().scl(-1.5f * len);
                float dst = n.dst(cc.x, cc.y);
                n.sub(cc.x, cc.y).scl((-2.5f) / 20f).limit(3f);
                cc.vx /= 1.5f;
                cc.vy /= 1.5f;
                cc.vr /= 1.5f;
                cc.vx += n.x;
                cc.vy += n.y;
                //cc.vr += -2f * side;
                cc.vr += (-5f * side) / (1f + dst / 5f);
            };
        }

        tmpVec.set(x1, y1).sub(x, y).rotate(-rotation);
        x1 = tmpVec.x / width + centerX;
        y1 = tmpVec.y / height + centerY;
        tmpVec.set(x2, y2).sub(x, y).rotate(-rotation);
        x2 = tmpVec.x / width + centerX;
        y2 = tmpVec.y / height + centerY;

        /*
        float cwx = centerX * width, cwy = centerY * height;
        tmpVec.set(x1, y1).sub(x + cwx, y + cwy).rotate(-rotation);
        x1 = tmpVec.x / width;
        y1 = tmpVec.y / height;
        tmpVec.set(x2, y2).sub(x + cwx, y + cwy).rotate(-rotation);
        x2 = tmpVec.x / width;
        y2 = tmpVec.y / height;
        */
        

        /*
        Effect eff = Fx.hitBulletColor;
        Color col = Tmp.c1.rand();
        Vec2 v = unproject(x1, y1);
        eff.at(v.x, v.y, 0, col);
        unproject(x2, y2);
        eff.at(v.x, v.y, 0, col);
        col.rand();
        eff.at(ox, oy, 0, col);
        */

        /*
        Tmp.v1.set(v.x, v.y).sub(x, y).rotate(-rotation);
        float vx = Tmp.v1.x / width + centerX;
        float vy = Tmp.v1.y / height + centerY;
        unproject(vx, vy);
        eff.at(v.x, v.y, 0, col.rand());
         */
        
        Seq<Severation> s = cut(x1, y1, x2, y2);
        if(!s.isEmpty()){
            for(Severation c : s){
                //if(c.area < 4f * 4f) continue;
                c.explosionEffect = explosionEffect;

                float dx = c.centerX - centerX;
                float dy = c.centerY - centerY;
                tmpVec.set(dx, dy).scl(width, height).rotate(rotation).add(x, y);

                c.rotation = rotation;
                c.x = tmpVec.x;
                c.y = tmpVec.y;
                c.vx += vx;
                c.vy += vy;

                force.get(c);

                c.add();
            }
            wasCut = true;
            remove();
        }
    }

    @Override
    public void update(){
        x += vx * Time.delta;
        y += vy * Time.delta;
        rotation += vr * Time.delta;
        
        zTime = Mathf.clamp(zTime + Time.delta / 40f);
        float drg = zTime < 1 ? drag : 0.2f;

        vx *= 1f - drg * Time.delta;
        vy *= 1f - drg * Time.delta;
        vr *= 1f - drg * Time.delta;

        if(time >= lifetime){
            float b = Mathf.sqrt(area / 4f);
            explosionEffect.at(x, y, b);
            float shake = b / 3f;
            Effect.shake(shake, shake, x, y);
            //if(explosionSound != Sounds.none) explosionSound.at(x, y, 1f, Mathf.clamp(b / 1.1f));
            if(explosionSound != Sounds.none) explosionSound.at(x, y, Mathf.random(0.9f, 1.1f) * Math.max(1f / (1f + (b - 8f) / 70f), 0.5f), Mathf.clamp(b / 1.1f));

            remove();
        }
        float speed = area < minArea ? 2f : 1f;
        time += Time.delta * speed;
    }

    @Override
    public void hitbox(Rect out){
        out.setCentered(x, y, bounds);
    }

    Vec2 unproject(float x, float y){
        return Tmp.v1.set((x - centerX) * width, (y - centerY) * height).rotate(rotation).add(this.x, this.y);
    }

    public void drawRender(){
        //return tmpVerts;
        //return null;
        float sin = Mathf.sinDeg(rotation);
        float cos = Mathf.cosDeg(rotation);
        float col = color, mcol = Color.clearFloatBits;
        TextureRegion r = region;

        for(CutTri t : tris){
            float[] pos = t.pos, verts = tmpVerts;
            int vertI = 0;
            for(int i = 0; i < 8; i += 2){
                int mi = Math.min(i, 4);

                float vx = (pos[mi] - centerX) * width;
                float vy = (pos[mi + 1] - centerY) * height;
                float tx = (vx * cos - vy * sin) + x;
                float ty = (vx * sin + vy * cos) + y;

                verts[vertI] = tx;
                verts[vertI + 1] = ty;
                verts[vertI + 2] = col;
                verts[vertI + 3] = Mathf.lerp(r.u, r.u2, pos[mi]);
                verts[vertI + 4] = Mathf.lerp(r.v2, r.v, pos[mi + 1]);
                verts[vertI + 5] = mcol;

                vertI += 6;
            }
            //Draw.z(trueZ);
            //Draw.vert(region.texture, verts, 0, 24);
            RenderGroupEntity.DrawnRegion reg = RenderGroupEntity.draw(Blending.normal, z, r.texture, verts, 0);
            reg.lifetime = 5f * 60f;
            reg.fadeCurveIn = 0.9f;
        }
    }

    @Override
    public void draw(){
        float sin = Mathf.sinDeg(rotation);
        float cos = Mathf.cosDeg(rotation);
        float col = color, mcol = Color.clearFloatBits;
        float oz = Draw.z();
        float sdz = shadowZ * (1 - zTime);

        TextureRegion r = region;
        Tmp.c1.set(Pal.shadow).a(Pal.shadow.a * Mathf.curve(zTime, 0f, 0.3f));
        float scol = Tmp.c1.toFloatBits();
        float trueZ = zTime < 1f ? z : (Layer.debris + 0.1f);

        //Draw.z(trueZ);
        for(CutTri t : tris){
            float[] pos = t.pos, verts = tmpVerts;
            int vertI = 0;
            for(int i = 0; i < 8; i += 2){
                int mi = Math.min(i, 4);

                float vx = (pos[mi] - centerX) * width;
                float vy = (pos[mi + 1] - centerY) * height;
                float tx = (vx * cos - vy * sin) + x;
                float ty = (vx * sin + vy * cos) + y;

                verts[vertI] = tx;
                verts[vertI + 1] = ty;
                verts[vertI + 2] = col;
                verts[vertI + 3] = Mathf.lerp(r.u, r.u2, pos[mi]);
                verts[vertI + 4] = Mathf.lerp(r.v2, r.v, pos[mi + 1]);
                verts[vertI + 5] = mcol;

                vertI += 6;
            }
            Draw.z(trueZ);
            Draw.vert(region.texture, verts, 0, 24);
            if(sdz > 0.001f){
                Draw.z(Math.min(trueZ - 1f, Layer.darkness));
                //float scol = Pal.shadow.toFloatBits();
                //Tmp.c1
                for(int i = 0; i < 4; i++){
                    int j = i * 6;
                    verts[j] += UnitType.shadowTX * sdz;
                    verts[j + 1] += UnitType.shadowTY * sdz;
                    verts[j + 2] = scol;
                }
                Draw.vert(region.texture, verts, 0, 24);
            }
        }
        /*
        Draw.color(Color.green);
        for(CutTri t : tris){
            float[] pos = t.pos;
            Lines.beginLine();
            for(int i = 0; i < 6; i += 2){
                //float x = pos[i], y = pos[i + 1];
                
                float vx = (pos[i] - centerX) * width;
                float vy = (pos[i + 1] - centerY) * height;
                float tx = (vx * cos - vy * sin) + x;
                float ty = (vx * sin + vy * cos) + y;
                
                Lines.linePoint(tx, ty);
            }
            Lines.endLine(true);
        }
        */
        Draw.color();

        Draw.z(oz);
    }

    @Override
    public float clipSize(){
        return bounds * 2f;
    }

    Seq<Severation> cut(float x1, float y1, float x2, float y2){
        tmpTris.clear();
        tmpTris2.clear();
        tmpCuts.clear();
        boolean hasCut = false;
        for(CutTri t : tris){
            Seq<CutTri> ts = t.cut(x1, y1, x2, y2);
            if(!ts.isEmpty()){
                hasCut = true;
            }else{
                tmpTris2.add(t);
            }
            tmpTris.addAll(ts);
        }
        if(hasCut){
            Severation side1 = new Severation(), side2 = new Severation();
            //side1.region = side2.region = region;
            side1.region.set(region);
            side2.region.set(region);
            side1.z = side2.z = z;
            side1.shadowZ = side2.shadowZ = shadowZ;
            side1.width = side2.width = width;
            side1.height = side2.height = height;
            side1.time = side2.time = (time / 2);
            side1.zTime = side2.zTime = zTime;

            side1.collided.addAll(collided);
            side2.collided.addAll(collided);

            side1.lifetime = (3f * 60f) + Mathf.range(15f);
            side2.lifetime = (3f * 60f) + Mathf.range(15f);

            side1.color = side2.color = color;

            for(CutTri tr : tmpTris){
                if(tr.side == 0){
                    side1.tris.add(tr);
                }else{
                    side2.tris.add(tr);
                }
            }
            for(CutTri tri : tmpTris2){
                float[] ps = tri.pos;
                int side = 0;
                for(int i = 0; i < 6; i += 2){
                    //mx += ps[i];
                    //my += ps[i + 1];
                    side += pointLineSide(x1, y1, x2, y2, ps[i], ps[i + 1]);
                }

                //int side = pointLineSide(x1, y1, x2, y2, mx / 6f, my / 6f);
                if(side >= 0){
                    side1.tris.add(tri);
                }else{
                    side2.tris.add(tri);
                }
            }
            side1.updateBounds();
            side2.updateBounds();
            tmpCuts.add(side1, side2);
        }
        return tmpCuts;
    }

    public void updateBounds(){
        float maxW = 0, minW = 1;
        float maxH = 0, minH = 1;
        float cx = 0, cy = 0;
        float tw = width, th = height;
        int cc = 0;
        area = 0f;
        for(CutTri t : tris){
            for(int i = 0; i < t.pos.length; i += 2){
                float x = t.pos[i], y = t.pos[i + 1];
                maxW = Math.max(x, maxW);
                minW = Math.min(x, minW);
                maxH = Math.max(y, maxH);
                minH = Math.min(y, minH);

                cx += x;
                cy += y;
                cc++;
            }
            float[] p = t.pos;
            area += triangleArea(p[0] * tw, p[1] * th, p[2] * tw, p[3] * th, p[4] * tw, p[5] * th);
        }
        float w = maxW - minW, h = maxH - minH;

        centerX = cx / cc;
        centerY = cy / cc;
        //minBounds = Math.min(w * width, h * height);
        bounds = Math.max(w * Math.abs(width), h * Math.abs(height));
    }

    @Override
    protected void addGroup(){
        super.addGroup();
        cutsSeq.add(this);
    }

    @Override
    protected void removeGroup(){
        super.removeGroup();
        cutsSeq.remove(this);
    }

    public static class CutTri{
        //0-1
        public float[] pos = new float[3 * 2];
        float area = 0f;
        //used for return
        int side = 0;

        Seq<CutTri> cut(float x1, float y1, float x2, float y2){
            intersections.clear();
            side1.clear();
            side2.clear();
            returnTri.clear();
            
            for(int i = 0; i < 3; i++){
                int i1 = i * 2;
                int i2 = ((i + 1) % 3) * 2;

                float lx1 = pos[i1], ly1 = pos[i1 + 1];
                float lx2 = pos[i2], ly2 = pos[i2 + 1];
                //intersectSegments
                if(intersectSegments(lx1, ly1, lx2, ly2, x1, y1, x2, y2, tmpVec)){
                //if(intersectSegments(lx1 * rsl, ly1 * rsl, lx2 * rsl, ly2 * rsl, x1 * rsl, y1 * rsl, x2 * rsl, y2 * rsl, tmpVec)){
                    intersections.add(tmpVec.x, tmpVec.y);
                }
            }
            //Log.info(intersections);
            if(intersections.size == 4){
                int within = 0;
                for(int i = 0; i < 6; i += 2){
                    float sx = pos[i], sy = pos[i + 1];
                    int side = pointLineSide(x1, y1, x2, y2, sx, sy);
                    if(side >= 0){
                        side1.add(sx, sy);
                    }else{
                        side2.add(sx, sy);
                    }
                    if(side == 0) within++;
                }
                if(side1.isEmpty() || side2.isEmpty() || within >= 2) return returnTri;

                for(int s = 0; s < 2; s++){
                    FloatSeq side = s == 0 ? side1 : side2;

                    /*
                    int id1 = side.size - 2;
                    float ix = side.items[id1];
                    float iy = side.items[id1 + 1];
                    float dst1 = 0f, dst2 = 0f;
                    
                    for(int i = 0; i < intersections.size; i += 2){
                        float dx = intersections.items[i] - ix, dy = intersections.items[i + 1] - iy;
                        float dst = dx * dx + dy * dy;
                        
                        if(i == 0){
                            dst1 = dst;
                        }else{
                            dst2 = dst;
                        }
                    }
                    if(dst1 < dst2){
                        side.add(intersections.items[0], intersections.items[1]);
                        side.add(intersections.items[2], intersections.items[3]);
                    }else{
                        side.add(intersections.items[2], intersections.items[3]);
                        side.add(intersections.items[0], intersections.items[1]);
                    }
                    */
                    if(side.size <= 2){
                        side.add(intersections.items[0], intersections.items[1]);
                        side.add(intersections.items[2], intersections.items[3]);
                    }else{
                        int lp = side.size - 2;
                        float fx = side.items[0], fy = side.items[1];
                        float lx = side.items[lp], ly = side.items[lp + 1];
                        int bias = 0;
                        
                        /*
                        for(int i = 0; i < 4; i += 2){
                            float dx = intersections.items[i], dy = intersections.items[i + 1];
                            //intersectSegments(lx1, ly1, lx2, ly2, x1, y1, x2, y2, tmpVec)
                            
                            
                            for(int j = 0; j < 4; j += 2){
                                float dx2 = intersections.items[j], dy2 = intersections.items[j + 1];
                                if((dx == dx2 && dy == dy2) || !intersectSegments(KFx, fy, dx, dy, lx, ly, dx2, dy2)){
                                //if((dx != dx2 || dy != dy2) && !intersectSegments(KFx, fy, dx, dy, lx, ly, dx2, dy2)){
                                    bias += (j == 0 ? 1 : -1);
                                }
                            }
                        }
                        */
                        float px1 = intersections.items[0], py1 = intersections.items[1];
                        float px2 = intersections.items[2], py2 = intersections.items[3];
                        for(int i = 0; i < 4; i += 2){
                            float dx = intersections.items[i], dy = intersections.items[i + 1];
                            
                            boolean intersect1 = intersectSegments(lx, ly, dx, dy, fx, fy, px1, py1, null);
                            boolean intersect2 = intersectSegments(lx, ly, dx, dy, fx, fy, px2, py2, null);
                            
                            if((dx != px1 || dy != py1) && intersect1){
                                bias++;
                            }
                            if((dx != px2 || dy != py2) && intersect2){
                                bias--;
                            }
                        }
                        if(bias >= 0){
                            side.add(intersections.items[0], intersections.items[1]);
                            side.add(intersections.items[2], intersections.items[3]);
                        }else{
                            side.add(intersections.items[2], intersections.items[3]);
                            side.add(intersections.items[0], intersections.items[1]);
                        }
                    }

                    if(side.size <= 6){
                        float[] items = side.items;
                        float area = triangleArea(items[0], items[1], items[2], items[3], items[4], items[5]);

                        if(area > 0f){
                            CutTri c = new CutTri();
                            c.side = s;
                            c.area = area;
                            System.arraycopy(side.items, 0, c.pos, 0, 6);
                            returnTri.add(c);
                        }
                    }else{
                        float[][] tr = triangulate(side.items, side.size / 2);
                        for(float[] ps : tr){
                            float area = triangleArea(ps[0], ps[1], ps[2], ps[3], ps[4], ps[5]);

                            if(area > 0f){
                                CutTri c = new CutTri();
                                c.side = s;
                                c.area = area;
                                System.arraycopy(ps, 0, c.pos, 0, 6);
                                returnTri.add(c);
                            }
                        }
                    }
                }
            }
            return returnTri;
        }

        float[][] triangulate(float[] arr, int size){
            float[][] ret = new float[size - 2][3 * 2];
            for(int i = 0; i < size - 2; i++){
                float[] ar = ret[i];
                int id = i * 2;
                float sx = arr[0], sy = arr[1];
                float px1 = arr[id + 2], py1 = arr[id + 3];
                float px2 = arr[id + 4], py2 = arr[id + 5];
                ar[0] = sx;
                ar[1] = sy;
                ar[2] = px1;
                ar[3] = py1;
                ar[4] = px2;
                ar[5] = py2;
            }
            return ret;
        }
    }

    static class Slash implements Poolable{
        float x1, y1, x2, y2;
        float time;
        int id = slashIDs++;

        @Override
        public void reset(){
            x1 = y1 = x2 = y2 = 0f;
            time = 0f;
            id = slashIDs++;
        }
    }
    /**
     * 网格切割 - 将碎片切割为 cols × rows 的网格
     * @param cols 列数
     * @param rows 行数
     * @param force 每个碎片的物理力回调
     * @return 切割后的碎片列表
     */
    public Seq<Severation> gridCut(int cols, int rows, Cons<Severation> force){
        // 添加详细日志
        Log.info("gridCut called: cols=" + cols + ", rows=" + rows + ", tris=" + tris.size);

        if(cols <= 0 || rows <= 0) {
            Log.warn("Invalid grid size");
            return Seq.with(this);
        }
        if(area < minArea || !added) {
            Log.warn("Area too small or not added: area=" + area + ", added=" + added);
            return Seq.with(this);
        }
        if(tris.isEmpty()) {
            Log.warn("No triangles");
            return Seq.with(this);
        }

        // 限制网格数量
        int maxGrids = 64;
        if(cols * rows > maxGrids){
            float ratio = Mathf.sqrt((float)maxGrids / (cols * rows));
            cols = Math.max(1, (int)(cols * ratio));
            rows = Math.max(1, (int)(rows * ratio));
            Log.info("Grid size reduced to " + cols + "x" + rows);
        }

        // 获取边界
        float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

        for(CutTri t : tris){
            for(int i = 0; i < 6; i += 2){
                minX = Math.min(minX, t.pos[i]);
                maxX = Math.max(maxX, t.pos[i]);
                minY = Math.min(minY, t.pos[i+1]);
                maxY = Math.max(maxY, t.pos[i+1]);
            }
        }

        float rangeX = maxX - minX;
        float rangeY = maxY - minY;
        if(rangeX < 0.001f || rangeY < 0.001f) {
            Log.warn("Range too small: " + rangeX + ", " + rangeY);
            return Seq.with(this);
        }

        // 创建网格
        float cellW = rangeX / cols;
        float cellH = rangeY / rows;

        // 存储结果
        Seq<Severation> results = new Seq<>();

        // 简化版本：将三角形分配到网格
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                Seq<CutTri> cellTris = new Seq<>();

                float cellMinX = minX + c * cellW;
                float cellMaxX = minX + (c + 1) * cellW;
                float cellMinY = minY + r * cellH;
                float cellMaxY = minY + (r + 1) * cellH;

                // 分配三角形到网格
                for(CutTri tri : tris){
                    // 检查三角形是否与网格单元相交
                    if (triIntersectsRect(tri, cellMinX, cellMaxX, cellMinY, cellMaxY)) {
                        // 简化：直接复制三角形（实际应该裁剪）
                        CutTri newTri = new CutTri();
                        System.arraycopy(tri.pos, 0, newTri.pos, 0, tri.pos.length);
                        newTri.area = tri.area;
                        cellTris.add(newTri);
                    }
                }

                if(cellTris.isEmpty()) continue;

                // 创建碎片
                Severation piece = createPieceFromTris(cellTris);
                if(piece == null) continue;

                // 计算网格中心
                float localCX = minX + (c + 0.5f) * cellW;
                float localCY = minY + (r + 0.5f) * cellH;

                // 转换到世界坐标
                Vec2 worldPos = localToWorld(localCX, localCY);
                piece.x = worldPos.x;
                piece.y = worldPos.y;
                piece.rotation = this.rotation;

                // 应用物理
                if(force != null){
                    force.get(piece);
                } else {
                    applyExplosionForce(piece, this.x, this.y);
                }

                results.add(piece);
            }
        }

        // 移除原碎片
        if(!results.isEmpty()){
            this.remove();
            Log.info("Grid cut produced " + results.size + " fragments");
        } else {
            Log.warn("Grid cut produced no fragments, keeping original");
            results.add(this);
        }

        return results;
    }

    /**
     * 检查三角形是否与矩形相交
     */
    private boolean triIntersectsRect(CutTri tri, float minX, float maxX, float minY, float maxY) {
        // 检查三角形顶点是否在矩形内
        for(int i = 0; i < 6; i += 2){
            float x = tri.pos[i];
            float y = tri.pos[i+1];
            if(x >= minX && x <= maxX && y >= minY && y <= maxY){
                return true;
            }
        }
        return false;
    }

    /**
     * 将三角形分配到网格单元（处理跨网格的情况）
     */
    /**
     * 将三角形分配到网格单元（处理跨网格的情况）
     */
    private Seq<CutTri> assignTriToGrid(CutTri tri, float minX, float minY,
                                        float cellW, float cellH, int cols, int rows){
        Seq<CutTri> result = new Seq<>();

        // 获取三角形的边界框
        float triMinX = Float.MAX_VALUE, triMaxX = -Float.MAX_VALUE;
        float triMinY = Float.MAX_VALUE, triMaxY = -Float.MAX_VALUE;
        for(int i = 0; i < 6; i += 2){
            triMinX = Math.min(triMinX, tri.pos[i]);
            triMaxX = Math.max(triMaxX, tri.pos[i]);
            triMinY = Math.min(triMinY, tri.pos[i+1]);
            triMaxY = Math.max(triMaxY, tri.pos[i+1]);
        }

        // 计算三角形覆盖的网格范围
        int startCol = Mathf.clamp((int)((triMinX - minX) / cellW), 0, cols - 1);
        int endCol = Mathf.clamp((int)((triMaxX - minX) / cellW), 0, cols - 1);
        int startRow = Mathf.clamp((int)((triMinY - minY) / cellH), 0, rows - 1);
        int endRow = Mathf.clamp((int)((triMaxY - minY) / cellH), 0, rows - 1);

        // 如果三角形完全在一个网格内，直接返回
        if(startCol == endCol && startRow == endRow){
            result.add(tri);
            return result;
        }

        // 先对三角形进行整体裁剪，然后分配到网格
        Seq<CutTri> currentTris = Seq.with(tri);

        // 按列裁剪（如果跨多列）
        if(startCol != endCol){
            Seq<CutTri> nextTris = new Seq<>();
            for(int col = startCol; col < endCol; col++){
                float cutX = minX + (col + 1) * cellW;
                for(CutTri t : currentTris){
                    // 裁剪到列边界
                    Seq<CutTri> clipped = clipTriByLine(t, cutX, true, 0);
                    nextTris.addAll(clipped);
                }
            }
            // 保留最后一列的三角形
            for(CutTri t : currentTris){
                // 检查三角形是否在最后一列
                boolean inLastCol = true;
                for(int i = 0; i < 6; i += 2){
                    if(t.pos[i] < minX + endCol * cellW){
                        inLastCol = false;
                        break;
                    }
                }
                if(inLastCol){
                    nextTris.add(t);
                }
            }
            currentTris = nextTris;
        }

        // 按行裁剪（如果跨多行）
        if(startRow != endRow && !currentTris.isEmpty()){
            Seq<CutTri> nextTris = new Seq<>();
            for(int row = startRow; row < endRow; row++){
                float cutY = minY + (row + 1) * cellH;
                for(CutTri t : currentTris){
                    Seq<CutTri> clipped = clipTriByLine(t, cutY, false, 0);
                    nextTris.addAll(clipped);
                }
            }
            // 保留最后一行的三角形
            for(CutTri t : currentTris){
                boolean inLastRow = true;
                for(int i = 0; i < 6; i += 2){
                    if(t.pos[i+1] < minY + endRow * cellH){
                        inLastRow = false;
                        break;
                    }
                }
                if(inLastRow){
                    nextTris.add(t);
                }
            }
            currentTris = nextTris;
        }

        // 将裁剪后的三角形分配到对应的网格
        for(CutTri clipped : currentTris){
            // 计算中心点确定网格位置
            float cx = (clipped.pos[0] + clipped.pos[2] + clipped.pos[4]) / 3f;
            float cy = (clipped.pos[1] + clipped.pos[3] + clipped.pos[5]) / 3f;

            int col = Mathf.clamp((int)((cx - minX) / cellW), 0, cols - 1);
            int row = Mathf.clamp((int)((cy - minY) / cellH), 0, rows - 1);

            // 只保留非空三角形
            float area = triangleArea(clipped.pos[0], clipped.pos[1],
                    clipped.pos[2], clipped.pos[3],
                    clipped.pos[4], clipped.pos[5]);
            if(area > 0.001f){
                clipped.area = area;
                result.add(clipped);
            }
        }

        return result;
    }

    /**
     * 用一条直线裁剪三角形（改进版）
     * @param tri 要裁剪的三角形
     * @param linePos 线的位置
     * @param clipX true: 垂直线, false: 水平线
     * @param side 保留哪一侧 (0: 保留右侧/上侧, 1: 保留左侧/下侧)
     */
    private Seq<CutTri> clipTriByLine(CutTri tri, float linePos, boolean clipX, int side){
        Seq<CutTri> result = new Seq<>();
        float[] vertices = tri.pos;

        // 检查三角形是否完全在一侧
        boolean allLeftOrBelow = true;
        boolean allRightOrAbove = true;
        for(int i = 0; i < 6; i += 2){
            float val = clipX ? vertices[i] : vertices[i+1];
            if(val < linePos) allRightOrAbove = false;
            else allLeftOrBelow = false;
        }

        // 如果完全在要保留的一侧
        if(side == 0 && allRightOrAbove){
            result.add(tri);
            return result;
        }
        if(side == 1 && allLeftOrBelow){
            result.add(tri);
            return result;
        }

        // 如果完全在要丢弃的一侧
        if(side == 0 && allLeftOrBelow) return result;
        if(side == 1 && allRightOrAbove) return result;

        // 使用 Sutherland-Hodgman 算法
        FloatSeq clippedPoints = new FloatSeq();

        for(int i = 0; i < 3; i++){
            int i1 = i * 2;
            int i2 = ((i + 1) % 3) * 2;

            float x1 = vertices[i1], y1 = vertices[i1+1];
            float x2 = vertices[i2], y2 = vertices[i2+1];

            float v1 = clipX ? x1 : y1;
            float v2 = clipX ? x2 : y2;

            boolean keep1 = (side == 0) ? (v1 >= linePos) : (v1 <= linePos);
            boolean keep2 = (side == 0) ? (v2 >= linePos) : (v2 <= linePos);

            if(keep1){
                clippedPoints.add(x1);
                clippedPoints.add(y1);
            }

            if(keep1 != keep2){
                // 计算交点
                float t = (linePos - v1) / (v2 - v1);
                t = Mathf.clamp(t, 0f, 1f);
                float ix = x1 + t * (x2 - x1);
                float iy = y1 + t * (y2 - y1);
                clippedPoints.add(ix);
                clippedPoints.add(iy);
            }
        }

        int pointCount = clippedPoints.size / 2;
        if(pointCount < 3) return result;

        // 三角剖分
        float[] pts = clippedPoints.items;
        if(pointCount == 3){
            CutTri newTri = new CutTri();
            for(int i = 0; i < 3; i++){
                newTri.pos[i*2] = pts[i*2];
                newTri.pos[i*2+1] = pts[i*2+1];
            }
            float area = triangleArea(newTri.pos[0], newTri.pos[1],
                    newTri.pos[2], newTri.pos[3],
                    newTri.pos[4], newTri.pos[5]);
            if(area > 0.001f){
                newTri.area = area;
                result.add(newTri);
            }
        } else if(pointCount > 3){
            for(int i = 1; i < pointCount - 1; i++){
                CutTri newTri = new CutTri();
                newTri.pos[0] = pts[0];
                newTri.pos[1] = pts[1];
                newTri.pos[2] = pts[i*2];
                newTri.pos[3] = pts[i*2+1];
                newTri.pos[4] = pts[(i+1)*2];
                newTri.pos[5] = pts[(i+1)*2+1];

                float area = triangleArea(newTri.pos[0], newTri.pos[1],
                        newTri.pos[2], newTri.pos[3],
                        newTri.pos[4], newTri.pos[5]);
                if(area > 0.001f){
                    newTri.area = area;
                    result.add(newTri);
                }
            }
        }

        return result;
    }

    /**
     * 裁剪三角形到指定范围（X或Y方向）
     */
    /**
     * 裁剪三角形到指定范围（X或Y方向）
     */
    private Seq<CutTri> clipTriToRange(CutTri tri, float min, float max, boolean clipX){
        Seq<CutTri> result = new Seq<>();
        float[] vertices = tri.pos;

        // 检查三角形是否完全在范围内
        boolean allInside = true;
        boolean allOutside = true;
        for(int i = 0; i < 6; i += 2){
            float val = clipX ? vertices[i] : vertices[i+1];
            if(val >= min && val <= max) allOutside = false;
            else allInside = false;
        }

        if(allInside){
            result.add(tri);
            return result;
        }
        if(allOutside){
            return result;
        }

        // 部分在范围内，使用Sutherland-Hodgman算法裁剪
        // 使用动态列表存储顶点，避免数组越界
        FloatSeq clippedPoints = new FloatSeq();

        for(int i = 0; i < 3; i++){
            int i1 = i * 2;
            int i2 = ((i + 1) % 3) * 2;

            float x1 = vertices[i1], y1 = vertices[i1+1];
            float x2 = vertices[i2], y2 = vertices[i2+1];

            float v1 = clipX ? x1 : y1;
            float v2 = clipX ? x2 : y2;

            boolean inside1 = v1 >= min && v1 <= max;
            boolean inside2 = v2 >= min && v2 <= max;

            if(inside1){
                clippedPoints.add(x1);
                clippedPoints.add(y1);
            }

            if(inside1 != inside2){
                // 计算交点
                float t = (min - v1) / (v2 - v1);
                if(t < 0 || t > 1) t = (max - v1) / (v2 - v1);
                // 限制t在[0,1]范围内
                t = Mathf.clamp(t, 0f, 1f);
                float ix = x1 + t * (x2 - x1);
                float iy = y1 + t * (y2 - y1);
                clippedPoints.add(ix);
                clippedPoints.add(iy);
            }
        }

        int pointCount = clippedPoints.size / 2;
        if(pointCount < 3) return result;

        // 三角剖分 - 使用动态方法处理任意数量的顶点
        if(pointCount == 3){
            CutTri newTri = new CutTri();
            for(int i = 0; i < 3; i++){
                newTri.pos[i*2] = clippedPoints.items[i*2];
                newTri.pos[i*2+1] = clippedPoints.items[i*2+1];
            }
            // 计算面积确保三角形有效
            float area = triangleArea(newTri.pos[0], newTri.pos[1],
                    newTri.pos[2], newTri.pos[3],
                    newTri.pos[4], newTri.pos[5]);
            if(area > 0.001f){
                newTri.area = area;
                result.add(newTri);
            }
        } else if(pointCount > 3){
            // 扇形三角剖分
            float[] pts = clippedPoints.items;
            for(int i = 1; i < pointCount - 1; i++){
                CutTri newTri = new CutTri();
                newTri.pos[0] = pts[0];
                newTri.pos[1] = pts[1];
                newTri.pos[2] = pts[i*2];
                newTri.pos[3] = pts[i*2+1];
                newTri.pos[4] = pts[(i+1)*2];
                newTri.pos[5] = pts[(i+1)*2+1];

                // 计算面积确保三角形有效
                float area = triangleArea(newTri.pos[0], newTri.pos[1],
                        newTri.pos[2], newTri.pos[3],
                        newTri.pos[4], newTri.pos[5]);
                if(area > 0.001f){
                    newTri.area = area;
                    result.add(newTri);
                }
            }
        }

        return result;
    }

    /**
     * 从三角形列表创建新的 Severation
     */
    private Severation createPieceFromTris(Seq<CutTri> tris){
        Severation piece = new Severation();
        piece.region.set(this.region);
        piece.width = this.width;
        piece.height = this.height;
        piece.rotation = this.rotation;
        piece.z = this.z;
        piece.shadowZ = this.shadowZ;
        piece.color = this.color;
        piece.lifetime = (3f * 60f) + Mathf.range(15f);
        piece.zTime = this.zTime;
        piece.collided.addAll(this.collided);

        // 复制三角形
        for(CutTri tri : tris){
            CutTri newTri = new CutTri();
            System.arraycopy(tri.pos, 0, newTri.pos, 0, tri.pos.length);
            newTri.area = tri.area;
            piece.tris.add(newTri);
        }

        piece.updateBounds();
        return piece;
    }

    /**
     * 将局部坐标转换为世界坐标
     */
    private Vec2 localToWorld(float lx, float ly){
        float cos = Mathf.cosDeg(rotation);
        float sin = Mathf.sinDeg(rotation);

        float vx = (lx - centerX) * width;
        float vy = (ly - centerY) * height;

        float wx = vx * cos - vy * sin + this.x;
        float wy = vx * sin + vy * cos + this.y;

        return Tmp.v1.set(wx, wy);
    }

    /**
     * 应用爆炸力（向外扩散）
     */
    /**
     * 改进的爆炸力应用 - 让碎片飞得更远
     */
    private void applyExplosionForce(Severation piece, float cx, float cy){
        // 计算从中心到碎片的方向
        float dx = piece.x - cx;
        float dy = piece.y - cy;
        float distance = Mathf.len(dx, dy);

        if(distance < 0.001f) {
            // 如果碎片在中心，随机方向
            float angle = Mathf.random(360f);
            piece.vx += Mathf.cosDeg(angle) * Mathf.random(2f, 5f);
            piece.vy += Mathf.sinDeg(angle) * Mathf.random(2f, 5f);
        } else {
            // 距离越远，速度越快（但有限制）
            float speed = Mathf.lerp(2f, 6f, Mathf.clamp(distance / (this.bounds * 0.5f)));
            speed *= Mathf.random(0.8f, 1.2f);

            float angle = Mathf.angle(dx, dy);
            piece.vx += Mathf.cosDeg(angle) * speed;
            piece.vy += Mathf.sinDeg(angle) * speed;
        }

        // 随机旋转
        piece.vr += Mathf.random(-15f, 15f);
    }

    /**
     * 更强的爆炸效果
     */
    public Seq<Severation> gridCutExplosive(int cols, int rows, float power){
        return gridCut(cols, rows, piece -> {
            // 计算从中心到碎片的方向
            float dx = piece.x - this.x;
            float dy = piece.y - this.y;
            float distance = Mathf.len(dx, dy);

            if(distance < 0.001f) {
                float angle = Mathf.random(360f);
                piece.vx += Mathf.cosDeg(angle) * power * Mathf.random(0.5f, 1f);
                piece.vy += Mathf.sinDeg(angle) * power * Mathf.random(0.5f, 1f);
            } else {
                float angle = Mathf.angle(dx, dy);
                float speed = power * (0.5f + distance / this.bounds);
                piece.vx += Mathf.cosDeg(angle) * speed;
                piece.vy += Mathf.sinDeg(angle) * speed;
            }

            // 强力旋转
            piece.vr += Mathf.random(-30f, 30f);

            // 不同碎片不同生命周期
            piece.lifetime += Mathf.random(-30f, 30f);
        });
    }
}
