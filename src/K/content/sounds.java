package K.content;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.Vars;

import java.lang.reflect.Field;

    public class sounds {
        public static ObjectMap<String, Sound> sounds = new ObjectMap<>();

        public static Sound
                alarm,charge,watching,dabian,dogshit,boom,hugeBlast,thunder,
                elaser,deslaser,deslasershoot,desnukehit,desnukehitfar,largebeamcharge,
                beamlarge,expr,shootbeam,beamstart,laser;
                ;

        public static void load() {
            charge = new Sound();
            alarm = new Sound();
            try {
                for (Field field : sounds.class.getFields()) {
                    if (field.getType().equals(Sound.class)) {
                        field.set(null, loadSound(field.getName()));
                    }
                }
            } catch (IllegalAccessException e) {
                Log.err(e);
            }
        }

        private static Sound loadSound(String soundName) {
            Sound sound = new Sound();
            if (Vars.headless) return sound;

            String path = "sounds/" + soundName;
            String filePath = Vars.tree.get(path + ".ogg").exists() ? path + ".ogg" : path + ".mp3";

            AssetDescriptor<?> desc = Core.assets.load(filePath, Sound.class, new SoundLoader.SoundParameter(sound));
            desc.errored = Throwable::printStackTrace;
            return sound;
        }
    }

