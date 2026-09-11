package K.content.Keycheck;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.util.Time;

public class CustomIconElement extends Element {
    private final int type;
    private float timer = 0f;

    public CustomIconElement(int type) {
        this.type = type;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timer += delta;
    }

    @Override
    public void draw() {
        // ✅ 检查自身的 visible 变量
        if (!visible) return;

        // ✅ 也检查父容器是否可见
        if (parent != null && !parent.visible) return;

        float x = this.x + width / 2f;
        float y = this.y + height / 2f;
        float size = Math.min(width, height);

        switch (type) {
            case 0: drawIcon0(x, y, size); break;
            case 1: drawIcon1(x, y, size); break;
            case 2: drawIcon2(x, y, size); break;
            case 3: drawIcon3(x, y, size); break;
        }
    }

    private void drawIcon0(float x, float y, float size) {
        Draw.color(Color.white);
        float fin = KeybindRody.F2t/KeybindRody.F2cd;
        TextureRegion jie = Core.atlas.find("kmod-jie");
        Draw.rect(jie,x,y,size,size);
        Draw.color(Color.black.a(0.5f));
        Fill.quad(x-size/2,y-size/2,x-size/2,(y-size/2)+size*fin,x+size/2,(y-size/2)+size*fin,x+size/2,y-size/2);
        Draw.reset();
    }

    private void drawIcon1(float x, float y, float size) {
        Draw.color(Color.white);
        float fin = KeybindRody.F3t/KeybindRody.F3cd;
        TextureRegion jie = Core.atlas.find("kmod-ba");
        Draw.rect(jie,x,y,size,size);
        Draw.color(Color.black.a(0.5f));
        Fill.quad(x-size/2,y-size/2,x-size/2,(y-size/2)+size*fin,x+size/2,(y-size/2)+size*fin,x+size/2,y-size/2);
        Draw.reset();
    }

    private void drawIcon2(float x, float y, float size) {
        Draw.color(Color.white);
        float fin = KeybindRody.F4t/KeybindRody.F4cd;
        TextureRegion jie = Core.atlas.find("kmod-kai");
        Draw.rect(jie,x,y,size,size);
        Draw.color(Color.black.a(0.5f));
        Fill.quad(x-size/2,y-size/2,x-size/2,(y-size/2)+size*fin,x+size/2,(y-size/2)+size*fin,x+size/2,y-size/2);
        Draw.reset();
    }

    private void drawIcon3(float x, float y, float size) {
        Draw.color(Color.white);
        float fin = KeybindRody.F5t/KeybindRody.F5cd;
        TextureRegion jie = Core.atlas.find("kmod-ly");
        Draw.rect(jie,x,y,size,size);
        Draw.color(Color.black.a(0.5f));
        Fill.quad(x-size/2,y-size/2,x-size/2,(y-size/2)+size*fin,x+size/2,(y-size/2)+size*fin,x+size/2,y-size/2);
        Draw.reset();
    }
}