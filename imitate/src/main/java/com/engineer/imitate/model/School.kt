package com.engineer.imitate.model

class School {
    class Location {
        var lat = 0f
        var lng = 0f
        override fun toString(): String {
            return "Location{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}'
        }
    }

    var name: String? = null
    var province: String? = null
    var city: String? = null
    var area: String? = null
    var address: String? = null
    var location: Location? = null
    override fun toString(): String {
        return "School{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", location=" + location +
                '}'
    }
}