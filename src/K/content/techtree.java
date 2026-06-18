package K.content;

import arc.struct.Seq;
import mindustry.game.Objectives;

import static K.content.blocks.*;
import static K.content.sector.zoneone;
import static mindustry.content.TechTree.*;

public class techtree {

    public static void load() {
        planets.nonepro.techTree = nodeRoot("none", planets.nonepro, () -> {
            nodeProduce(items.Feces, () -> {
            });
            node(blocks.fecesdrill, Seq.with(new Objectives.Research(items.Feces)), () -> {}
            );
            node(blocks.fecesconveyor, Seq.with(new Objectives.Research(items.Feces)), () -> {
                node(fecesjunction, () -> {
                    node(fecesrouter, () -> {
                        node(fecesbridgeconveyor, () -> {}
                        );
                        node(fecessorter, () -> {
                            node(fecesoverflowGate, () -> {}
                            );
                            node(fecesunderflowGate, () -> {}
                            );
                                }
                        );
                        node(fecesinvertedSorter, () -> {}
                        );
                            }
                    );
                        }
                );

            }
            );
            node(zoneone, () -> {}
            );
            node(fecespress, Seq.with(new Objectives.Research(items.Feces)), () -> {
                node(Rody_neutron_centrifuge, () -> {}
                );
                    }
            );
            node(Simple_fecal_incinerator, () -> {});
        });
    }
}
