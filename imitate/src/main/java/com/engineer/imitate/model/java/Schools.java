package com.engineer.imitate.model.java;

import java.util.List;

public class Schools {
    private int id = 0;
    private String province;
    private List<School> schoolList;
    private int type = 0;
    private String code = "aa";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<School> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<School> schoolList) {
        this.schoolList = schoolList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Schools(province=" + province + ", schoolList=" + schoolList + ")";
    }


    public static class School {

        public static class Location {
            private float lat = 0f;
            private float lng = 0f;

            public float getLat() {
                return lat;
            }

            public void setLat(float lat) {
                this.lat = lat;
            }

            public float getLng() {
                return lng;
            }

            public void setLng(float lng) {
                this.lng = lng;
            }

            @Override
            public String toString() {
                return "Location{" + "lat=" + lat + ", lng=" + lng + '}';
            }
        }

        private String name;
        private String province;
        private String city;
        private String area;
        private String address;
        private Location location;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return "School{" + "name='" + name + '\'' + ", province='" + province + '\'' + ", city='" + city + '\'' + ", area='" + area + '\'' + ", address='" + address + '\'' + ", location=" + location + '}';
        }
    }
}
