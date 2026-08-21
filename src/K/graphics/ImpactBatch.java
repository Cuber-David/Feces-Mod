package K.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;

public class ImpactBatch extends SpriteBatch {
    // 自定义临时数组，替代不可访问的 tmpVertices
    private final float[] tempVerts = new float[SPRITE_SIZE]; // SPRITE_SIZE=24

    public float u, v, u2, v2;
    public float rx, ry;            // 可删除
    public float lastRotation;      // 可删除
    public boolean heavyShader = false;
    public boolean useColor = false;
    public boolean canChangeShader = true;
    private boolean white = false;

    public static ImpactBatch batch;
    static Batch lastBatch;

    public static void init() {
        batch = new ImpactBatch();
    }

    public static void beginSwap() {
        lastBatch = Core.batch;
        Mat proj = Draw.proj(), trans = Draw.trans();
        Core.batch = batch;
        Draw.proj(proj);
        Draw.trans(trans);
    }

    public static void endSwap() {
        Draw.flush();
        Core.batch = lastBatch;
    }

    public void setWhite(boolean w) {
        if (white != w) {
            flush();
            white = w;
        }
    }

    public Texture getTexture() {
        return lastTexture;
    }

    public ImpactBatch() {
        super(4096); // 或自定义大小
    }

    @Override
    protected void draw(Texture texture, float[] spriteVertices, int offset, int count) {
        // 传入的 spriteVertices 是 6 属性（x,y,color,u,v,mixColor）
        // 我们需要修改颜色后调用 drawSuper
        int remaining = count;
        int srcPos = offset;
        while (remaining > 0) {
            int batchSize = Math.min(remaining, tempVerts.length);
            System.arraycopy(spriteVertices, srcPos, tempVerts, 0, batchSize);
            modifyColors(tempVerts, 0, batchSize);
            drawSuper(texture, tempVerts, 0, batchSize);
            remaining -= batchSize;
            srcPos += batchSize;
        }
    }

    @Override
    protected void draw(TextureRegion region, float x, float y, float originX, float originY,
                        float width, float height, float rotation) {
        Texture texture = region.texture;
        if (texture != lastTexture) {
            switchTexture(texture);
        } else if (idx == buffer.capacity()) {
            flush();
        }

        // 计算颜色
        float color = useColor ? this.colorPacked : Color.whiteFloatBits;
        float mixColor = white ? Color.whiteFloatBits : Color.blackFloatBits;

        // 使用自定义 tempVerts 构建顶点
        float u = region.u;
        float v = region.v2;
        float u2 = region.u2;
        float v2 = region.v;
        float[] verts = tempVerts;
        int pos = 0;

        if (!Mathf.zero(rotation)) {
            float worldOriginX = x + originX;
            float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            float cos = Mathf.cosDeg(rotation);
            float sin = Mathf.sinDeg(rotation);

            float x1 = cos * fx - sin * fy + worldOriginX;
            float y1 = sin * fx + cos * fy + worldOriginY;
            float x2 = cos * fx - sin * fy2 + worldOriginX;
            float y2 = sin * fx + cos * fy2 + worldOriginY;
            float x3 = cos * fx2 - sin * fy2 + worldOriginX;
            float y3 = sin * fx2 + cos * fy2 + worldOriginY;
            float x4 = x1 + (x3 - x2);
            float y4 = y3 - (y2 - y1);

            verts[pos] = x1;         verts[pos+1] = y1;
            verts[pos+2] = color;    verts[pos+3] = u;    verts[pos+4] = v;    verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = x2;         verts[pos+1] = y2;
            verts[pos+2] = color;    verts[pos+3] = u;    verts[pos+4] = v2;   verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = x3;         verts[pos+1] = y3;
            verts[pos+2] = color;    verts[pos+3] = u2;   verts[pos+4] = v2;   verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = x4;         verts[pos+1] = y4;
            verts[pos+2] = color;    verts[pos+3] = u2;   verts[pos+4] = v;    verts[pos+5] = mixColor;
        } else {
            float fx2 = x + width;
            float fy2 = y + height;

            verts[pos] = x;          verts[pos+1] = y;
            verts[pos+2] = color;    verts[pos+3] = u;    verts[pos+4] = v;    verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = x;          verts[pos+1] = fy2;
            verts[pos+2] = color;    verts[pos+3] = u;    verts[pos+4] = v2;   verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = fx2;        verts[pos+1] = fy2;
            verts[pos+2] = color;    verts[pos+3] = u2;   verts[pos+4] = v2;   verts[pos+5] = mixColor;
            pos += 6;
            verts[pos] = fx2;        verts[pos+1] = y;
            verts[pos+2] = color;    verts[pos+3] = u2;   verts[pos+4] = v;    verts[pos+5] = mixColor;
        }

        // 写入 buffer
        buffer.put(tempVerts, 0, SPRITE_SIZE);
        this.idx += SPRITE_SIZE;

        // 记录纹理区域信息（可选）
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.lastRotation = rotation;
    }

    // 修改顶点数组中的颜色（6属性/顶点）
    private void modifyColors(float[] verts, int offset, int count) {
        float color = useColor ? this.colorPacked : Color.whiteFloatBits;
        float mixColor = white ? Color.whiteFloatBits : Color.blackFloatBits;
        for (int i = offset; i < offset + count; i += VERTEX_SIZE) {
            verts[i + 2] = color;   // 主色
            verts[i + 5] = mixColor; // 混合色
        }
    }

    @Override
    protected void setShader(Shader shader, boolean apply) {
        if (!canChangeShader) return;
        super.setShader(shader, apply);
    }
}