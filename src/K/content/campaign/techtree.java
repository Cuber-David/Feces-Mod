package K.content.campaign;

import K.content.blocks;
import K.content.items;
import K.content.liquids;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.game.Objectives;

import static K.content.blocks.*;
import static K.content.campaign.sector.*;
import static K.content.KUnitTypes.*;
import static mindustry.content.TechTree.*;

public class techtree {

    public static void load() {
        planets.nonepro.techTree = nodeRoot("none", planets.nonepro, () -> {
            nodeProduce(items.Feces, () -> {
                        nodeProduce(items.Constipated_feces, () -> {
                            nodeProduce(items.Rody_neutron, () -> {
                                nodeProduce(items.Rody_proton, () -> {
                                    nodeProduce(items.Rody_atom, () -> {
                                    });
                                });
                                nodeProduce(items.Rody_electron, () -> {
                                });
                            });
                            nodeProduce(liquids.urine, () -> {
                                nodeProduce(liquids.ionic_liquid, () -> {
                                    nodeProduce(liquids.fecalwater, () -> {
                                    });
                                });
                            });
                        });
                    });
            node(fecesprojector, Seq.with(new Objectives.Research(items.Feces),
                                          new Objectives.Research(items.Constipated_feces),
                                          new Objectives.Research(Items.lead)), () -> {
                node(ionicpulsecannon, Seq.with(new Objectives.Research(liquids.ionic_liquid)), () -> {
                    node(fecesrain, Seq.with(new Objectives.Research(Items.silicon)), () -> {
                        node(protonbeam, Seq.with(new Objectives.Research(items.Rody_proton)), () -> {}
                        );
                            }
                    );
                        }
                );
                    }
            );
            TechTree.node(blocks.fecesdrill, Seq.with(new Objectives.Research(items.Feces)), () -> {
                node(ionicdrill, Seq.with(new Objectives.Research(Items.silicon)), () -> {}
                );
                    }
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
                    node(fecesunloader, Seq.with(new Objectives.Research(Items.silicon)), () -> {}
                    );
                        }
                );
                        node(neutronconveyor, Seq.with(new Objectives.Research(items.Rody_neutron)), () -> {
                            node(neutronjunction, () -> {
                                node(neutronrouter, () -> {
                                    node(neutronbridgeconveyor, () -> {}
                                    );
                                        }
                                );
                                    }
                            );
                                }
                        );

            }
            );
            node(zoneone, () -> {
                node(zonetwo, Seq.with((new Objectives.SectorComplete(zoneone))), () -> {
                    node(zonethree, Seq.with((new Objectives.SectorComplete(zonetwo))), () -> {
                        node(zonefour, Seq.with((new Objectives.SectorComplete(zonethree)),
                                                 new Objectives.Research(Helperdrone)), () -> {
                            node(zonefive, Seq.with((new Objectives.SectorComplete(zonefour)),
                                            new Objectives.Research(items.Rody_neutron)), () -> {}
                            );
                                }
                        );
                            }
                    );
                        }
                );
                    }
            );
            node(Fecaldrone, Seq.with(new Objectives.Research(fecescore)), () -> {
                node(Fecalwarcraft, Seq.with(new Objectives.Research(fecesbasion)), () -> {}
                );
                    }
            );
            node(HelpFactory, Seq.with(new Objectives.OnSector(zonetwo)), () -> {
                node(Plasmadrill, () -> {}
                );
                node(Helperdrone, () -> {}
                );
                node(TaticalFactory, Seq.with(new Objectives.Research(Items.silicon),
                                              new Objectives.OnSector(zonefour)), () -> {
                    node(Tacticalassaultmecha, () -> {}
                    );
                    node(Tacticalsuppressiontank, () -> {}
                    );
                        }
                );
                node(testfactory, Seq.with(new Objectives.SectorComplete(zonefour),
                                           new Objectives.Research(Items.silicon)), () -> {
                    node(Testspider, () -> {}
                    );
                    node(Flyingfortress, () -> {}
                    );
                    node(Conceptualhovertank, () -> {}
                    );
                    node(Testtank, () -> {}
                    );
                    node(Firebeedrone, () -> {}
                    );
                    node(Combatengineer, () -> {}
                    );
                        }
                );
            }
            );
            node(feceswall, Seq.with(new Objectives.Research(items.Feces)), () -> {
                node(feceswallbig, () -> {
                    node(Rody_neutronwall, Seq.with(new Objectives.Research(items.Rody_proton)), () -> {
                        node(Rody_wall, Seq.with(new Objectives.Research(items.Rody_atom)), () -> {}
                        );
                            }
                    );
                        }
                );
                    }
            );
            node(fecespress, Seq.with(new Objectives.Research(items.Feces)), () -> {
                node(Ionic_liquid_factory, Seq.with(new Objectives.OnSector(zonetwo),
                                                       new Objectives.Research(liquids.urine)), () -> {
                    node(fecespulverizer, Seq.with(new Objectives.OnSector(zonethree),
                            new Objectives.Research(Items.scrap),
                            new Objectives.Research(Items.coal)), () -> {
                        node(fecessiliconfactory, Seq.with(new Objectives.Research(Items.sand)), () -> {
                            node(Rody_neutron_centrifuge, Seq.with(new Objectives.Research(Items.silicon),
                                                                   new Objectives.SectorComplete(zonefour)), () -> {
                                node(largecentrifuge, Seq.with(new Objectives.Research(items.Rody_neutron)), () -> {});
                                node(hugefecespress, () -> {}
                                );
                                node(Ionic_liquid_factory_big, () -> {}
                                );
                                    }
                            );
                            node(Particle_diverter_device, Seq.with(new Objectives.Research(items.Rody_neutron),
                                                                    new Objectives.OnSector(zonefive)), () -> {
                                node(Atomic_aggregation_device, Seq.with(new Objectives.Research(items.Rody_proton),
                                                                         new Objectives.Research(items.Rody_electron)), () -> {}
                                );
                                    }
                            );
                                }
                        );
                            }
                    );
                        }
                );
                node(fecalwatermixer, Seq.with(new Objectives.Research(liquids.urine),
                                               new Objectives.OnSector(zonethree)), () -> {}
                );
                    }
            );
            node(Simple_fecal_incinerator, Seq.with(new Objectives.Research(items.Feces),
                                                    new Objectives.OnSector(zonetwo)), () -> {
                node(fecespole, () -> {
                    node(fecalsteamgenerator, () -> {

                    });
                    node(fecesbattery, () -> {
                        node(Rody_reactor, () -> {

                        });
                    });
                    node(fecessolarpenal, Seq.with(new Objectives.Research(Items.silicon)), () -> {

                    });
                });
            });
            node(fecescore, Seq.with(new Objectives.SectorComplete(zoneone)), () -> {
                node(fecesbasion, Seq.with(new Objectives.Research(Items.silicon)), () -> {
                        });
                node(fecesmend, Seq.with(new Objectives.OnSector(zonethree),
                                         new Objectives.Research(items.Feces)), () -> {
                    node(protectshield, Seq.with(new Objectives.Research(items.Rody_electron)), () -> {}
                    );
                        }
                );
                    }
            );
            node(fecesvault,Seq.with(new Objectives.SectorComplete(zonetwo)),  () -> {}
            );
            node(fecespump, Seq.with(new Objectives.OnSector(zonetwo)), () -> {
                node(fecesconduit, () -> {
                            node(fecesconduitjunction, () -> {
                                        node(fecesconduitrouter, () -> {
                                                    node(fecesconduitbridge, () -> {
                                                        node(fecesconduitsorter, () -> {}
                                                        );
                                                            }
                                                    );}
                                        );}
                            );}
                );
                node(fecestank,Seq.with(new Objectives.OnSector(zonetwo)),  () -> {}
                );
            });
            node(Bigdagger, Seq.with(new Objectives.SectorComplete(zoneone)), () -> {
                node(Bignova, Seq.with(new Objectives.SectorComplete(zonetwo)), () -> {
                    node(Bigcrawler, Seq.with(new Objectives.SectorComplete(zonethree)), () -> {}
                    );
                        }
                );
                    }
            );
            node(Thunder, Seq.with(new Objectives.SectorComplete(zonefive)), () -> {
                node(Rody, Seq.with(new Objectives.SectorComplete(zonefive)), () -> {}
                );
                    }
            );
        });
    }
}
