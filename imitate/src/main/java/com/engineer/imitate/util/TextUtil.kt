package com.engineer.imitate.util

/**
 * @author rookie
 * @since 01-14-2020
 */
object TextUtil {

    fun getJson(): String {
        return jsonArray(
            json {
                "id" to "0001"
                "type" to "donut"
                "name" to "Cake"
                "ppu" to 0.55
                "batters" to json {
                    "batter" to jsonArray(
                        json {
                            "id" to "1001"
                            "type" to "Regular"
                        },
                        json {
                            "id" to "1002"
                            "type" to "Chocolate"
                        },
                        json {
                            "id" to "1003"
                            "type" to "Blueberry"
                        },
                        json {
                            "id" to "1004"
                            "type" to "Devil's Food"
                        }
                    )
                }
                "topping" to jsonArray(
                    json {
                        "id" to "5001"
                        "type" to "None"
                    },
                    json {
                        "id" to "5002"
                        "type" to "Glazed"
                    },
                    json {
                        "id" to "5005"
                        "type" to "Sugar"
                    },
                    json {
                        "id" to "5007"
                        "type" to "Powdered Sugar"
                    },
                    json {
                        "id" to "5006"
                        "type" to "Chocolate with Sprinkles"
                    },
                    json {
                        "id" to "5003"
                        "type" to "Chocolate"
                    },
                    json {
                        "id" to "5004"
                        "type" to "Maple"
                    }
                )
            },
            json {
                "id" to "0002"
                "type" to "donut"
                "name" to "Raised"
                "ppu" to 0.55
                "batters" to json {
                    "batter" to jsonArray(
                        json {
                            "id" to "1001"
                            "type" to "Regular"
                        }
                    )
                }
                "topping" to jsonArray(
                    json {
                        "id" to "5001"
                        "type" to "None"
                    },
                    json {
                        "id" to "5002"
                        "type" to "Glazed"
                    },
                    json {
                        "id" to "5005"
                        "type" to "Sugar"
                    },
                    json {
                        "id" to "5003"
                        "type" to "Chocolate"
                    },
                    json {
                        "id" to "5004"
                        "type" to "Maple"
                    }
                )
            },
            json {
                "id" to "0003"
                "type" to "donut"
                "name" to "Old Fashioned"
                "ppu" to 0.55
                "batters" to json {
                    "batter" to jsonArray(
                        json {
                            "id" to "1001"
                            "type" to "Regular"
                        },
                        json {
                            "id" to "1002"
                            "type" to "Chocolate"
                        }
                    )
                }
                "topping" to jsonArray(
                    json {
                        "id" to "5001"
                        "type" to "None"
                    },
                    json {
                        "id" to "5002"
                        "type" to "Glazed"
                    },
                    json {
                        "id" to "5003"
                        "type" to "Chocolate"
                    },
                    json {
                        "id" to "5004"
                        "type" to "Maple"
                    }
                )
            }
        ).toString(4)
    }
}