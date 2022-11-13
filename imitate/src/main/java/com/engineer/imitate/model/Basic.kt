package com.engineer.imitate.model

import com.zdog.library.render.*

class Basic {
//    val shapes = mutableListOf<World>()

//    init {
//        // Line
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = -40f),
//                    line(x = 40f)
//                )
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Z shape
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = -32f, y = -40f),
//                    line(x = 32f, y = -40f),
//                    line(x = -32f, y = 40f),
//                    line(x = 32f, y = 40f)
//                )
//                closed = false
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // 3D shape
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = -32f, y = -40f, z = 40f),
//                    line(x = 32f, y = -40f),
//                    line(x = 32f, y = 40f, z = 40f),
//                    line(x = 32f, y = 40f, z = -40f)
//                )
//                closed = false
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Arc
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = -60f, y = -60f),
//                    arc(
//                        vector(x = 20f, y = -60f),
//                        vector(x = 20f, y = 20f)
//                    ),
//                    arc(
//                        vector(x = 20f, y = 60f),
//                        vector(x = 60f, y = 60f)
//                    )
//                )
//                closed = false
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Bezier
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = -60f, y = -60f),
//                    bezier(
//                        vector(x = 20f, y = -60f),
//                        vector(x = 20f, y = 60f),
//                        vector(x = 60f, y = 60f)
//                    )
//                )
//                closed = false
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Closed
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = 0f, y = -40f),
//                    line(x = 40f, y = 40f),
//                    line(x = -40f, y = 40f)
//                )
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Unclosed
//        shapes.add(World().apply {
//            shape {
//                addTo = illo
//                path(
//                    move(x = 0f, y = -40f),
//                    line(x = 40f, y = 40f),
//                    line(x = -40f, y = 40f)
//                )
//                closed = false
//                stroke = 20f
//                color = "#636"
//            }
//        })
//
//        // Hemisphere
//        shapes.add(World().apply {
//            hemisphere {
//                addTo = illo
//                diameter = 120f
//                stroke = 0f
//                color = "#C25"
//                backface = "#EA0"
//            }
//        })
//
//        // Cone
//        shapes.add(World().apply {
//            cone {
//                addTo = illo
//                diameter = 70f
//                length = 90f
//                stroke = 0f
//                color = "#636"
//                backface = "#C25"
//            }
//        })
//
//        // Cylinder1
//        shapes.add(World().apply {
//            cylinder {
//                addTo = illo
//                diameter = 80f
//                length = 120f
//                stroke = 0f
//                color = "#C25"
//                backface = "#E62"
//            }
//        })
//
//        // Cylinder2
//       shapes.add(World().apply {
//           cylinder {
//               addTo = illo
//               diameter = 80f
//               length = 120f
//               stroke = 0f
//               color = "#C25"
//               frontFace = "#EA0"
//               backface = "#636"
//           }
//       })
//
//        // Box1
//        val box = box {
//            width = 120f
//            height = 100f
//            depth = 80f
//            stroke = 0f
//            color = "#C25"
//            leftFace = "#EA0"
//            rightFace = "#E62"
//            topFace = "#ED0"
//            bottomFace = "#636"
//        }
//
//        shapes.add(World().apply {
//            addChild(box)
//        })
//
//        // Box2
//        shapes.add(World().apply {
//            box.copy {
//                addTo = illo
//                leftFace = null
//                rightFace = null
//                rearFace = "#EA0"
//            }
//        })
//
//        val distance = 40f
//
//        val dot = shape {
//            translate(y = -distance)
//            stroke = 80f
//            color = "#636"
//        }
//
//        shapes.add(World().apply {
//            illo.rotate(x = -(TAU / 16).toFloat())
//            addChild(dot)
//
//            dot.copy {
//                translate(x = -distance)
//                color = "#EA0"
//            }
//            dot.copy {
//                translate(z = distance)
//                color = "#C25"
//            }
//            dot.copy {
//                translate(x = distance)
//                color = "#E62"
//            }
//            dot.copy {
//                translate(z = -distance)
//                color = "#C25"
//            }
//            dot.copy {
//                translate(y = distance)
//            }
//        })
//
//        shapes.add(World().apply {
//            illo.rotate(x = -(TAU / 16).toFloat())
//            val dot2 = dot.copy {
//                addTo = illo
//            }
//            dot2.copy {
//                translate(x = -distance)
//            }
//            dot2.copy {
//                translate(z = distance)
//            }
//            dot2.copy {
//                translate(x = distance)
//            }
//            dot2.copy {
//                translate(z = -distance)
//            }
//            dot2.copy {
//                translate(y = distance)
//            }
//        })
//
//        shapes.forEach {
//            it.apply {
//                play(
//                    illo.rotateTo(y = TAU.toFloat()).duration(3000).repeat()
//                )
//            }
//        }
//    }
}