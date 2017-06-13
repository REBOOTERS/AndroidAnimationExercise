package home.smart.fly.animations.adapter;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by co-mall on 2017/5/17.
 */

public class SchoolBeanShell implements Serializable{

    /**
     * id : 1
     * school : [{"id":1001,"name":"清华大学"},{"id":1002,"name":"北京大学"},{"id":1003,"name":"中国人民大学"},{"id":1004,"name":"北京航空航天大学"},{"id":1005,"name":"北京邮电大学"},{"id":1006,"name":"北京师范大学"},{"id":1007,"name":"中国传媒大学"},{"id":1008,"name":"北京语言大学"},{"id":1009,"name":"北京科技大学"},{"id":1010,"name":"中国农业大学"},{"id":1011,"name":"北京理工大学"},{"id":1012,"name":"北京林业大学"},{"id":1013,"name":"北京交通大学"},{"id":1014,"name":"中国矿业大学（北京）"},{"id":1015,"name":"北京信息科技大学"},{"id":1016,"name":"北京工业大学"},{"id":1017,"name":"北京化工大学"},{"id":1018,"name":"中国政法大学"},{"id":1019,"name":"对外经贸大学"},{"id":1020,"name":"中央民族大学"},{"id":1021,"name":"中国地质大学（北京）"},{"id":1022,"name":"中科院"},{"id":1023,"name":"北京中医药大学"},{"id":1024,"name":"首都经济贸易大学"},{"id":1025,"name":"中央财经大学"},{"id":1026,"name":"北方工业大学"},{"id":1027,"name":"中国石油大学（北京）"},{"id":1028,"name":"外交学院"},{"id":1029,"name":"首都师范大学"},{"id":1030,"name":"中央戏剧学院"},{"id":1031,"name":"中国青年政治学院"},{"id":1032,"name":"北京外国语大学"},{"id":1033,"name":"华北电力大学（北京）"},{"id":1034,"name":"中国人民公安大学"},{"id":1035,"name":"北京协和医学院"},{"id":1036,"name":"北京体育大学"},{"id":1037,"name":"北京工商大学"},{"id":1038,"name":"北京联合大学"},{"id":1039,"name":"首都医科大学"},{"id":1040,"name":"国际关系学院"},{"id":1041,"name":"中央美术学院"},{"id":1042,"name":"北京电子科技学院"},{"id":1043,"name":"中国劳动关系学院"},{"id":1044,"name":"中华女子学院"},{"id":1045,"name":"北京建筑工程学院"},{"id":1046,"name":"北京印刷学院"},{"id":1047,"name":"北京石油化工学院"},{"id":1048,"name":"首钢工学院"},{"id":1049,"name":"北京农学院"},{"id":1050,"name":"首都体育学院"},{"id":1051,"name":"北京第二外国语学院"},{"id":1052,"name":"北京物资学院"},{"id":1053,"name":"北京警察学院"},{"id":1054,"name":"中央音乐学院"},{"id":1055,"name":"中国戏曲学院"},{"id":1056,"name":"北京舞蹈学院"},{"id":1057,"name":"北京城市学院"},{"id":1058,"name":"北京电影学院"},{"id":1059,"name":"北京服装学院"},{"id":1060,"name":"青岛教育学院"},{"id":1061,"name":"北京体育职业学院"},{"id":1062,"name":"中国人民解放军装甲兵工程学院"},{"id":1067,"name":"中国石油勘探开发研究院"},{"id":1068,"name":"北京生命科学研究所"},{"id":1069,"name":"中国电影资料馆"},{"id":1070,"name":"北京工商大学嘉华学院"},{"id":1071,"name":"首都师范大学科德学院"},{"id":1072,"name":"北京工业大学耿丹学院"},{"id":1073,"name":"北京化工大学北方学院"},{"id":1074,"name":"北京联合大学广告学院"},{"id":1075,"name":"北京邮电大学世纪学院"},{"id":1076,"name":"北京国际商务学院"},{"id":1101,"name":"北京大学医学部"},{"id":1102,"name":"北京政法职业学院"},{"id":1103,"name":"北京信息职业技术学院"},{"id":1104,"name":"北京现代职业技术学院"},{"id":1105,"name":"北京现代音乐研修学院"},{"id":1106,"name":"北京戏曲艺术职业学院"},{"id":1107,"name":"北京锡华国际经贸职业学院"},{"id":1108,"name":"北京盛基艺术学校"},{"id":1109,"name":"北京培黎职业学院"},{"id":1110,"name":"北京农业职业学院"},{"id":1111,"name":"北京科技职业学院"},{"id":1112,"name":"北京科技经营管理学院"},{"id":1113,"name":"北京经贸职业学院"},{"id":1114,"name":"北京经济技术职业学院"},{"id":1115,"name":"北京京北职业技术学院"},{"id":1116,"name":"北京交通职业技术学院"},{"id":1117,"name":"北京吉利大学"},{"id":1118,"name":"北京汇佳职业学院"},{"id":1119,"name":"北京工业职业技术学院"},{"id":1120,"name":"北京工商管理专修学院"},{"id":1121,"name":"北京电子科技职业学院"},{"id":1122,"name":"北京财贸职业学院"},{"id":1123,"name":"北京北大方正软件技术学院"},{"id":1124,"name":"北大资源美术学院"},{"id":1125,"name":"北京人文大学"},{"id":1126,"name":"北京高等秘书学院"},{"id":1127,"name":"北京应用技术大学"},{"id":1128,"name":"中国防卫科技学院"},{"id":1129,"name":"中国音乐学院"},{"id":1130,"name":"中国信息大学"},{"id":1131,"name":"北京青年政治学院"},{"id":1132,"name":"北京财经专修学院"},{"id":1133,"name":"北京经济管理职业学院"},{"id":1134,"name":"北京美国英语语言学院"},{"id":1135,"name":"中国管理软件学院"},{"id":1136,"name":"财政部财政科学研究所"},{"id":1137,"name":"北大资源学院"},{"id":1138,"name":"现代管理大学"},{"id":1139,"name":"北京民族大学"},{"id":1140,"name":"北京市劳动保障职业学院"},{"id":1141,"name":"北京市建设职工大学"},{"id":1142,"name":"北京市房地产职工大学"},{"id":1143,"name":"北京市汽车工业总公司职工大学"},{"id":1144,"name":"北京市西城经济科学大学"},{"id":1145,"name":"北京市丰台区职工大学"},{"id":1146,"name":"北京广播电视大学"},{"id":1147,"name":"北京教育学院"},{"id":1148,"name":"北京市东城区职工业余大学"},{"id":1149,"name":"北京市总工会职工大学"},{"id":1150,"name":"北京市海淀区职工大学"},{"id":1151,"name":"北京市崇文区职工大学"},{"id":1152,"name":"北京宣武红旗业余大学"},{"id":1153,"name":"北京市石景山区业余大学"},{"id":1154,"name":"北京市朝阳区职工大学"},{"id":1155,"name":"北京市机械工业局职工大学"},{"id":1156,"name":"北京医药集团职工大学"},{"id":1157,"name":"北京劳动保障职业学院"},{"id":1158,"name":"北京社会管理职业学院"},{"id":1160,"name":"北京演艺专修学院"},{"id":1161,"name":"北京兴华大学"},{"id":1162,"name":"北京新园明职业学院"},{"id":1163,"name":"中央党校研究生院"},{"id":1164,"name":"中国社科院"},{"id":1165,"name":"北京旅游专修学院"},{"id":1166,"name":"东方文化艺术学院"},{"id":1167,"name":"首都联合职工大学"},{"id":1168,"name":"中国农业科学院"},{"id":1169,"name":"北京影视研修学院"},{"id":1170,"name":"国家法官学院"},{"id":1171,"name":"北京建设大学"},{"id":1172,"name":"北京金融学院"},{"id":1173,"name":"北京黄埔大学"},{"id":1174,"name":"中瑞酒店管理学院"},{"id":1175,"name":"中国建筑设计研究院"},{"id":1176,"name":"北京文理研修学院"},{"id":1177,"name":"北京当代艺术学院"},{"id":1178,"name":"北京大学国际法学院"},{"id":1179,"name":"北京交通运输职业学院"},{"id":1180,"name":"中国艺术研究院"},{"id":1181,"name":"北京工业大学通州分校"},{"id":1182,"name":"北京八维研修学院"},{"id":1998,"name":"网络销售大学"},{"id":1999,"name":"朝阳二外"},{"id":2000,"name":"中北国际演艺学校"}]
     * name : 北京
     */

    private int id;
    private String name;
    private LatLng mLatLng;
    private List<SchoolBean> school;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SchoolBean> getSchool() {
        return school;
    }

    public void setSchool(List<SchoolBean> school) {
        this.school = school;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public static class SchoolBean implements Serializable{
        /**
         * id : 1001
         * name : 清华大学
         */
        private LatLng mLatLng;
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LatLng getLatLng() {
            return mLatLng;
        }

        public void setLatLng(LatLng latLng) {
            mLatLng = latLng;
        }
    }
}
