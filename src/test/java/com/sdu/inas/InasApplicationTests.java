package com.sdu.inas;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdu.inas.beans.*;
import com.sdu.inas.controller.ExtractionController;
import com.sdu.inas.controller.ParamController;
import com.sdu.inas.controller.RawinfoController;
import com.sdu.inas.repository.EventRepository;
import com.sdu.inas.repository.HbaseDao;
import com.sdu.inas.service.ObjectService;
import com.sdu.inas.service.impl.ObjectServiceImpl;
import com.sdu.inas.util.HbaseModelUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InasApplicationTests {

    @Autowired
    ParamController paramController;

    @Autowired
    HbaseDao hbaseDao;

    @Autowired
    ObjectService objectService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ExtractionController extractionController;

    @Autowired
    ObjectServiceImpl objectServiceImpl;

    @Autowired
    RawinfoController rawinfoController;

    ArrayList<String> list = new ArrayList<>(Arrays.asList("丁汝昌738INFOB=5", "丘吉尔Z9R992MG=7", "东乡平八郎72JR4LO6=3", "严凤英NVYAIRA7=3",
            "严复WC0VRLK9=4", "丰子恺BI6VIATL=3", "乐天宇YQP437DW=3", "乔治·华盛顿48GPEKNM=4", "乔治三世MHCLT04O=3", "乔治六世IGN51VD8=4",
            "习近平611E3M3Q=8", "于右任JMETMLK4=3", "亚伯拉罕·林肯1ORPKTBH=6", "亚历山大一世GVIKCZZ2=3", "亚历山大大帝YIOTUIR8=4",
            "亚当·斯密56OY97C4=3", "亨利·福特392IEQCG=3", "亨利·贝克勒尔C3MPEEL4=3", "亨利一世QX1ORRRG=3", "亨利三世EPEFOGR4=3",
            "亨利二世LWGEP96L=3", "亨利五世PS0YMKNJ=3", "亨利八世QFUJ825T=7", "亨利四世HOH86W7Y=5", "任弼时H4US1P3O=4",
            "伊丽莎白二世JDNDFW7G=4", "伍修权7MDTDMZV=3", "伍德罗·威尔逊WSC4D8AE=3", "伏尔泰9D1TJWF5=3", "伏尼契04QYQCY4=3",
            "伽利略PGU4XSPU=5", "但丁2NQOFF60=3", "何其芳TSBOKPAM=3", "何厚铧TMFWLV2D=3", "何香凝U6YWV1UB=3", "佛朗哥8B562XPW=4",
            "侯宝林RKYI44VD=3", "侯德榜Y3J55TDZ=3", "侯赛因ISM9JY4J=4", "保罗六世K4EC7UX5=3", "俞平伯2FVLULPP=3", "俾斯麦N1MW6OPA=4",
            "傅作义5HNVSP3U=7", "傅抱石614S1WGB=3", "傅秋涛C8LJDCXM=3", "元世祖2NW6FBQD=7", "元太祖PR7S9MWN=3", "光绪帝ZH7ETPUY=7",
            "克林顿0ZGGLPMM=14", "全斗焕V650S8RF=4", "冯云山B8PMU5FS=4", "冯如4IFTKKO3=3", "冯子材R99CA08K=3", "冯玉祥I1YLOX2Q=15",
            "冯雪峰TGGCALJL=3", "冼星海8C2SX7SI=4", "凯恩斯T73RM74L=4", "凯末尔E1UHYI5I=4", "列夫·托尔斯泰OJ8P95YE=3", "列宁AEJKO1Y6=5",
            "刘伯承PV6FUZZT=8", "刘半农XVADN8TN=4", "刘天华OI2WHBZM=3", "刘少奇5ANS4708=11", "刘晓庆8GV05XH0=3", "刘永福WIRMKETZ=3",
            "刘海粟5CETZZ1F=6", "刘炳森RR6S4E22=3", "刘秀8JAYG1BP=3", "刘翔QF86RLOS=4", "利奥波德二世C0GGFD1P=3", "利玛窦VROKMYBC=4",
            "加米涅夫DAI7SLIL=3", "勃兰特MUV73QNV=5", "勃列日涅夫EXFVS8AP=4", "包拯6S8YOWGD=3", "华国锋9T9MP2LN=4", "卓别林O1TBO008=7",
            "卓娅DXLFFTAM=4", "南唐后主8ICLHB3P=3", "博卡萨T5KW6FNG=4", "卡尔·车尔尼BZEQYPT4=3", "卡彭YCMMHNE1=3", "卡拉什尼科夫506TESQV=3",
            "卡斯特罗ISCLNNR5=5", "卢梭0CD8LQC2=3", "卢泰愚5ZHNN398=5", "卢瑟福E9B6F1FV=3", "卢蒙巴KFK2ZBDZ=3", "叶乔波DC4J18F2=3",
            "叶利钦FWWMO8TY=9", "叶剑英UFDBM6EP=3", "叶圣陶OV50GADW=3", "叶挺0DBQYCMK=5", "叶芝S8CFLV68=3", "司徒雷登POIHWEYN=7",
            "司马光5PFTQ5QH=4", "吉列26440E5P=3", "吉鸿昌JYQVH1F7=4", "吴佩孚2Y2Z6JH7=6", "吴健雄IFIDS9P9=4", "吴印咸NS1NQPKW=4",
            "吴庭艳XBMFSAWC=4", "吴稚晖T1FD6AYP=4", "周培源H0153ZAM=4", "周学熙J2PCXFFC=3", "周恩来NQ55HX8V=31", "咸丰帝1FHQGMAF=3",
            "哈勃UKQS0LLV=3", "哈尼OZOWU2RI=3", "哈罗德·威尔逊8Z6OAWOJ=3", "哈马舍尔德62E1UIGX=3", "哥伦布UV2QHGAX=6", "哥白尼SIUVOF9W=3",
            "哥穆尔卡GDBUK4BX=3", "唐太宗2BTQCHAP=3", "唐玄宗OLUU0G2K=3", "唐继尧TQWJXXRO=3", "唐高宗34U00GUE=6", "图拉真UO09HMFR=3",
            "埃以W8DGN43W=4", "埃克森OXHHAMZY=3", "埃德加·德加KO85E0SJ=3", "埃德加·斯诺12MHXD9S=3", "埃斯科瓦尔VE7MBZLI=4", "基辛格AFDPGGYL=6",
            "塔利班GLAKNV3S=3", "塔夫脱·哈特莱6LGM810U=3", "塞万提斯A3CKPH9V=3", "墨索里尼3X2HY27Z=5", "夏承焘CDW8XBBH=3", "夏衍0EGOHPY1=3",
            "夏鼐61Y5F2I4=3", "大仲马MM48X54O=3", "契诃夫F1KEKRKK=3", "奥利弗·克伦威尔7D6X8VA4=3", "奥尔布赖特IPHA1APE=3", "奥巴马Y0AKLAN0=9",
            "奥托大帝ZVP5OECU=3", "奥黛丽·赫本LFX1BBQK=3", "姆贝基C4INUHPP=3", "威尔第D6BCU3BD=3", "威廉·冯特LQYL4ZK4=3", "威廉一世PCCH0GU3=4",
            "威廉王子5ROYL1G0=3", "孔子D57DGH2L=3", "孔繁森P45SWEL8=3", "孙中山YXCWDG9Z=39", "孙传芳43OUWNEZ=5", "孙殿英RRBRVFFY=5", "孙立人8HJVDXHF=3",
            "安倍晋三Y1QGPW2C=5", "安德罗波夫E47S0P2L=4", "安道尔QLUOVAFQ=3", "安阳CWENOMU9=3", "宋仁宗DZHOAGEH=3", "宋太祖RWC6MNSA=4", "宋子文47Y2OJZP=3",
            "宋孝宗TJKK7AAU=3", "宋庆龄K9W5TNVI=14", "宋徽宗6C1MGNIT=3", "宋武帝DT35KUOX=3", "宋神宗F215Q2IZ=4", "宋美龄U1Q77GJ2=4", "宋高宗47ELTHX3=3",
            "宣统帝DNMA7PHG=10", "容闳2GZMJJDJ=4", "密特朗EVCYNY7V=5", "小林多喜二LIM2KI9Y=4", "小泉A570N4IZ=3", "小渊惠三ZKYGV2AN=4",
            "尼克松L3OR6E8G=13", "尼古拉二世V226OQZK=3", "尼赫鲁50RJP5N4=8", "尼采57Y9DFLH=3", "居里夫人HHRN2W95=5", "屠呦呦808EADC3=3",
            "屠格涅夫XKY0TBV7=3", "岸信介TTN1I2O9=3", "川端康成H9W3MT8L=3", "左宗棠4H71S5TW=7", "巴尔扎克IUC85RAB=3", "巴拉克·奥巴马ZX8XFBZJ=4",
            "巴西DYT13ZON=10", "巴金EV4KP28C=3", "巴顿DCVE9RDX=3", "布什TYBBSA7M=8", "布哈林CG9JE9CI=4", "布托2K974C6Y=3", "布莱希特0Z3EXAAR=3",
            "布隆迪D37SXRXA=3", "希区柯克AR3B45AC=3", "希拉克50UE67IZ=3", "希特勒LQ2TX9RD=19", "帕格尼尼3XFWKXBX=3", "帖木儿CHBTPB04=3",
            "席勒JX2RRH36=3", "干祖望NIJ0NAXB=3", "康德J3LOVQUK=3", "康有为AQP7D8JW=4", "康生RXI081CH=3", "廖仲恺LNJKX0KC=3", "廖承志67HV3KBA=5",
            "弗兰西斯·培根VUQLMO4M=3", "弗洛伊德N29Q2OP0=3", "弗莱明NYNLUEHB=4", "张之洞BO6Q7D9B=8", "张伯伦TEHVBJEN=3", "张作霖LX4T62RG=9",
            "张国焘Y5HJGYQX=8", "张士诚OM17QUGE=3", "张太雷5OM5ALXT=3", "张学良N779UABX=12", "张宗昌BRYCNGUB=3", "张居正R00J6C7D=3", "张澜YTR21T5G=3",
            "张献忠23YDW17T=3", "张自忠S5S61JJ6=3", "张艺谋BVEH6AEG=3", "张謇V7RDMKW6=3", "张闻天QQ8V0Z1E=5", "彗星SW2Z5ECY=3", "彭德怀WOBA3A1M=11",
            "彼得一世T0LGLUPP=3", "徐世昌QS4N399K=4", "徐向前S0CV08KU=3", "徐寿H5JOG2N5=3", "徐悲鸿PEGZEHJY=3", "徐特立LON0S97J=3", "徐锡麟S5YH77TH=4",
            "德克勒克0MLE29MN=3", "德川家康S6BNSAII=3", "德沃夏克Z4PUFVXJ=3", "德莱塞ECLV9GLI=3", "德雷福斯冤案CZISE0CN=3", "恩格斯K067AL7H=4",
            "恩维尔·霍查PV8MD92I=3", "慈禧I7A204ZZ=8", "戈尔巴乔夫O3XHSEB8=7", "戚继光RSOCV1RX=3", "戴季陶HITOF6FB=3", "戴安娜B3DFM31Z=3",
            "戴安娜王妃YB1JBHQ6=3", "戴尔·卡内基YU8J73HN=3", "戴高乐K697MCI1=10", "扎伊尔OKCDKUM4=3", "托斯卡尼尼MM0D3GGF=3", "托洛茨基HI3XXLA7=4",
            "托马斯·曼R7OGHVW2=4", "托马斯·爱迪生K17WAZVG=3", "托马斯·莫尔B7HNK6BE=3", "拉什迪Y4MJ5U2I=3", "拉吉夫·甘地UUN9B69L=4", "拉宾TTYI057W=3",
            "拉瓦锡GZADLFHD=3", "拉脱维亚3CR5VQL5=3", "拉马克O7ZVJKYX=3", "拜伦HIUWEQ2Y=3", "拿破仑·波拿巴IAS1RLW3=11", "挪威KND8K0JC=6",
            "捷克斯洛伐克IO9YBDNY=4", "捷尔任斯基IQUJ9FI3=3", "提图斯HGFLCLUU=3", "摩尔根8P2TGVTI=3", "摩根3ZKLQ7SJ=4", "撒切尔夫人KLO0KO64=8",
            "斯蒂芬逊9NUCWSUQ=3", "斯诺GW569VPU=3", "方志敏OT8M800Q=3", "方腊YIX9SKEP=3", "旭烈兀21PPC4I5=3", "昂尼斯EMVJZWXJ=3", "昆仑关7VM2PRQ8=4",
            "明十三陵WE156RU4=3", "明太祖JJK2LD0K=8", "明惠帝KM2R4WKE=3", "明成祖QWMJRUYF=7", "明治天皇O5RLNJ6V=3", "晏紫PBR5XKWC=3",
            "普京GQBULP4O=4", "普利策Z66MTMNA=3", "普密蓬Y34EIR0E=3", "普希金MD1QM1N7=3", "普朗克Y8ZFSVOM=4", "普赖斯T0Y4BUXS=3", "普鲁士H1WJ7LPO=7",
            "智利LOEREPWD=4", "曹操LBW0E9JA=3", "曹汝霖46YI107C=3", "曹锟HRU91JPV=4", "曹雪芹NUZ84Q8S=3", "曼努埃尔二世NOK581KF=3", "曼德拉CWZVJSXV=11",
            "曾国藩ZMNGZ1NG=5", "曾荫权IILZZJSI=3", "朗缪尔G1Y0VVOZ=3", "未知IM6S4XKZ=8", "朱可夫LHN2K5W8=6", "朱塞普·皮亚齐CHTHB1S1=3", "朱建华VOJJAR8R=3",
            "朱德JBMKVNYL=12", "朱温0E5I0EID=4", "朱自清MZOFXK9T=3", "朱镕基OIA1LK77=4", "李先念BR1ATH5D=4", "李善兰48REDYFV=3", "李嘉诚IIGK6L0W=4",
            "李四光DEWARF3I=3", "李大钊XXLHX9YN=7", "李宁WSQDSY0B=3", "李宗仁R61TAZ4N=9", "李小龙IF5PNZ1F=4", "李德GZPA9ZGX=3", "李惠堂FA22WFH6=3",
            "李成桂IJIT56NV=3", "李承晚QMW78T8K=4", "李普曼8Y3GX4B4=3", "李渔ZL3Y0CF5=3", "李登辉8MUTPO5B=4", "李白8YYTSJR1=3", "李秀成5FAR2J1W=3",
            "李立三2XOMUHKO=5", "李自成JDW3YTX5=3", "李闻1TFVNA2S=3", "李隆基C7IJXXGS=3", "李鸿章KQZM8Y3O=11", "杜月笙SIB05VDD=4", "杜鲁门5CCBNJLF=5",
            "杨嘉墀DQ0HQAZP=3", "杨度KL4SVN01=3", "杨玉环EDQ9864O=4", "杨虎城E9WN6TT0=5", "杨靖宇Q8I7DMIC=3", "杰基·罗宾森F26QB648=3", "杰斐逊3ZUVI3EF=3",
            "林则徐6B7M6P6I=7", "林彪KGZ8X3S2=9", "林登·约翰逊H1UD3H7E=3", "林肯2YK5B8PC=3", "查理一世6G3NH4H7=3", "查理七世F0IQNB10=3",
            "查理九世UXQM1B0I=3", "查理五世V83LCINR=4", "查韦斯SPZ3FSJ1=3", "柯南道尔WONR3IJR=3", "柯受良H2G4PFJP=3", "柳亚子XAU91I0L=4",
            "柴可夫斯基W2QQU0Z9=3", "格什温G1FRE52S=3", "格拉芙825MDA90=3", "格林卡9SV1IYJJ=3", "格林纳达ZSMEN4GC=4", "格瓦拉XI2X4VHY=3",
            "格蕾丝·凯利7ZGCFI22=3", "格里格PRWOUSM4=4", "桑兰HL98N41K=3", "桓温343E0XYM=4", "桥本龙太郎1PRXV10S=3", "梁启超BMFFTQR7=8",
            "梅兰芳GQ2K6UE8=5", "梅杰TG5HOT4B=3", "梅里美TO8KTMZT=3", "梵蒂冈EZ8RJ9AF=3", "欧·亨利FB5ZS241=3", "欧姆ACPX1RGK=3",
            "武元甲T0H9N2JF=3", "武则天RM2CVNG1=7", "段祺瑞7Q9181EF=10", "比尔·盖茨HWLMWFV7=3", "毕加索WTCE46YS=5", "毛姆G978XBTI=3",
            "毛泽东MC5C628O=101", "江泽民1DXD5V9Y=22", "江西TQ1SABZQ=3", "江青KFLU1SNE=12", "汤因比RL8REEGU=3", "汤姆·艾金斯QW14AEID=3",
            "汤显祖8T2KL77O=4", "汤飞凡DR7FPN9N=3", "汪精卫PKXLOSWY=20", "沃尔特·迪斯尼SHOVVH76=3", "沈从文S5Z6OOI5=3", "沈崇QT5GZM6W=3",
            "法尔科内G0MNOA9N=3", "法拉第UJCBZP6B=4", "法捷耶夫0OPFCXOR=3", "波列斯瓦夫一世LK92H490=3", "泰戈尔QYOKXQ8Z=4", "泰森IULUU5A3=4",
            "洛克菲勒2AC2NIXP=3", "洪升KTAGHPAQ=3", "洪秀全KZK0QIPI=3", "洪都拉斯BXRT749G=3", "海伦·凯勒VJRM10AZ=4", "海明威AK0ZQWI8=4",
            "清世宗RYFO5DBC=6", "清仁宗CCB34Y1E=3", "清圣祖9YIPX2O8=7", "清太祖XE36KJC5=3", "清宣宗K99LJ2L5=4", "清政府3IEZOGU5=46",
            "清高宗L4GN4XU8=3", "温家宝RA2EFZ4S=4", "潘汉年LAXACH5R=3", "瀛海威IM3363DF=3", "焦裕禄EXDAJTTH=4", "爱因斯坦AYQRQ7TG=5",
            "爱德华三世ORSMR95U=3", "爱德华八世3WLSZ7B8=3", "爱达·勒芙蕾丝1KLNCXSX=3", "爱迪生OJS8Y1D0=9", "特拉维夫BLRT1YDC=3", "特里萨嬷嬷G93UW0MV=3",
            "王云五2Z6GQG03=3", "王力T57VVDIV=3", "王国维L2AA8FJF=3", "王安石WH1ZIOJT=3", "王明PNSI8KUF=4", "王淦昌JAC9F76M=3", "王稼祥NZQ4CSO9=3",
            "王茂荫8KFICMWR=3", "王贞治UERQN0WX=3", "王选H96C58KY=3", "玛丽一世VWIWWKTB=4", "玛丽莲·梦露PETAJU4I=3", "玻利瓦尔F3PORXQI=3", "玻利维亚P2UMBF3B=4",
            "玻尔JMV4X6X7=3", "班达拉奈克夫人GAFJ2MOT=3", "琴纳T8G3P0BH=3", "琼瑶S8UOM4YV=3", "瑞典FHZ45ALM=5", "瓦加斯NUB2T48N=3", "瓦尔德海姆RHM0TX3P=4",
            "瓦格纳32KJ82QG=3", "甘肃LGYW7SH8=4", "田中角荣0Y32QLVB=6", "白朗WFO2VCEP=4", "盛宣怀M4EWEXOC=4", "真纳3DB7X13J=3", "睿亲王4XL1NMBR=3",
            "瞿秋白GANU7C4A=5", "石达开F14OMUFB=4", "福州9SDVC5Y7=3", "福特PN4HYFII=8", "福田康夫4P50FIBN=3", "福禄培尔F1XPHODT=3", "秀兰·邓波儿0777HV08=3",
            "秋瑾YYHVKF9D=3", "科尔3ZUXS3SO=5", "科拉松·阿基诺33VJKQ6I=3", "科特迪瓦8ZIX3XPV=3", "秦仲文X3HEHFNP=3", "程潜5P6HPWT4=3", "穆巴拉克XFQ9OK9A=3",
            "章士钊LN2JM4MN=3", "章太炎WTWX9RLY=3", "章炳麟40XVJ589=7", "童第周4X7Y0SQF=3", "竹下登ZK0V2Y69=3", "第谷·布拉赫K7NCN7FA=3", "米丘林QANRR49E=3",
            "米开朗琪罗T8VLQ9TB=3", "粟裕6W92Y7XI=4", "索尔仁尼琴K3NTFSE7=6", "索摩查IXW6A30T=3", "索罗斯BO14K40J=4", "约翰·韦恩H7MBP724=3", "约翰逊O72Q53SL=4",
            "细川护熙JPRMXIDN=3", "罗伯特·布鲁斯BCPIY8CX=3", "罗振玉19LG7ZA8=3", "罗斯福K4WUM8MQ=10", "罗瑞卿Q6ABK5QK=3", "罗素R1JDC9LS=4", "罗纳尔多7XALMVRA=3",
            "罗荣桓GV6M41J9=3", "聂卫平31WM67XH=4", "聂耳6UL4OA9I=3", "聂荣臻9VWFX648=4", "聂鲁达K3XRXZZN=3", "肖斯塔科维奇00DCQ2KQ=3", "肖洛霍夫S7GV9MMH=3",
            "胡厥文G9X20W81=3", "胡安·卡洛斯VNN1OX5O=3", "胡宗南NI505EWU=4", "胡志明QKZC6V1Z=4", "胡汉民0HI90QX3=3", "胡秋原YIN8PHGO=3", "胡耀邦S5GAZZ23=3",
            "胡适XBNGZBCM=11", "胡锦涛YG79PBN3=13", "舒曼LOO8BUPQ=4", "艾伯特9EHGBC69=3", "艾希曼XHFTT7FE=3", "艾森豪威尔H5O0PA5Q=3", "范仲淹CVGPEK5Y=4",
            "范志毅FC50RMX6=3", "范长江0JTSVHC6=3", "茅以升J53UHY7C=3", "茅盾L5FA3OOL=3", "荣德生QMEDSRS7=3", "莎士比亚SON1CLW7=3", "莫扎特WQ320BLJ=4",
            "莫泊桑ZAJ5FWL7=3", "莫洛托夫OHRUJX8R=3", "莫言ATMTV7C1=3", "菲尔普斯OI8ZUIDE=3", "菲茨杰拉德T15TZE27=3", "萧三XI3YTD6M=3", "萧伯纳61G81884=3",
            "萧红JJS5KVOP=3", "萨兰BUEJWO1W=3", "萨尔瓦多UK8RZCMZ=3", "萨特22DQT6PQ=3", "萨达特75UBCYZS=3", "萨马兰奇MVB024O7=3", "葛丽泰·嘉宝TE65JUSR=3",
            "葛兰西GWSIPCR2=3", "董其武GRVX2RHW=3", "董卓4680MDI9=4", "蒋介石9VJDWOJ3=54", "蒋筑英OPMIPVWB=3", "蒋经国LNC4W9T2=3", "蒙博托BNWLN9BB=3",
            "蒙巴顿0NJY13CJ=3", "蓬皮杜BLX65PGL=3", "蔡元培MF8OKX6F=5", "蔡和森F1ATWCDV=4", "蔡锷HMWY28WW=7", "藤森9L8NCD5V=3", "袁世凯CIUPPOYN=24",
            "袁牧之9DRZGY86=3", "袁隆平0WUU98AH=5", "褚民谊HUO5ZXA6=3", "西奥多·赖曼BTRHFTGS=3", "西贝柳斯S2ZZCN7G=3", "詹天佑3CVRX6B5=3", "许德珩S2X9WDMI=3",
            "许涤新YG7YI27S=3", "诸葛亮DLWOFHKP=3", "谈家桢5QYD1HLC=3", "谢军58O69N82=3", "谢晋元6ND7DIYK=3", "谢瓦尔德纳泽DUXDIHFF=3", "谢苗·米哈伊洛维奇·布琼尼FKEDAPL2=3",
            "谢里夫FWOEIZGL=3", "谭鑫培SW7EOG2M=3", "贝京QUVOE1XI=4", "贝利9UKVSHV9=5", "贝多芬DRCYBI82=3", "贝娜齐尔·布托J8VJCV1Y=3", "贝尔德QE8HFXO5=3",
            "贝当VJWKEG5S=6", "贝鲁特Q5R73I7G=4", "贞德4Y4VY4P6=3", "费孝通Z2KG9YWP=3", "贺龙AQKE4ZGM=4", "贾莱·古柏Y4MD9TAQ=3", "赖伐尔IIA84F1O=3",
            "赛珍珠K8ZRQGEK=3", "赫尔利X1RKWEP2=4", "赫斯GG26M8XT=3", "赫胥黎S6NF8TXN=3", "赫鲁晓夫NW3S2I58=14", "赵世炎DOGZM781=3", "赵丹P4ZUCXHJ=3",
            "赵树理GH5NLJG6=3", "路易十五X9SUZX44=3", "路易十六ZHED7L2J=4", "路易十四MGBN220U=3", "车尔尼雪夫斯基KU39K4KQ=3", "辛弃疾JZDD0Z1R=3", "辜振甫OGZYOXCB=3",
            "辽太祖U9NI3EKQ=3", "辽景宗X2K32KO9=3", "达·伽马MX3CQRV9=3", "达·芬奇US8ZPI84=3", "达尔文8UF6P18R=4", "迈克尔·杰克逊OXQN9RX3=3", "连战M395CZDW=3",
            "邓中夏TF6XFYQK=3", "邓丽君5GGMH3R7=3", "邓子恢FURWKUWY=3", "邓小平KUS13AAO=63", "邓演达JFNZOIF4=3", "邓稼先VXD04J2V=3", "邓肯79HIQNB4=5", "邓颖超41GG9TQM=4",
            "邱少云2Y10CFWA=3", "邵力子187X09EG=3", "邵飘萍7X8KPVRM=3", "邹容6WNIUGNZ=4", "邹韬奋E1QQ708M=3", "郎平QUMZ5I14=3", "郎朗JKIFVZNW=3", "郑和XJG20XQH=3",
            "郑成功30MY8W8I=6", "郑洁B29UY97X=3", "郭守敬CHC1EVPI=3", "郭沫若YSS1VTR5=5", "里根CGMNEDXS=6", "金圣叹QP2SVHF8=3", "金大中SABRO4QR=5", "金庸9MFUGUFC=3",
            "金日成8OPFTJQP=9", "金正日T6KD76X2=4", "金泳三H2SQZJEN=3", "鉴真IFSV75K8=5", "钱三强ZRT117QA=3", "钱学森8JB4K0JG=5", "钱玄同4RMYBK5Z=3", "钱穆RJ48S5YV=3",
            "钱钟书9R5BT7D3=3", "铁托607WC6ZW=4", "闻一多4NEOS1HA=4", "阎锡山MUWGXV2Z=9", "阿兰图灵E655W2RW=3", "阿姆斯特朗FAD4X2M5=3", "阿方索一世ZWVHO4EA=3", "阿炳GWWROVFL=4",
            "阿登纳MGM2OQKU=3", "阿蒙森P9HKH7ZF=4", "阿骨打3ZQPGOSF=3", "陀思妥耶夫斯基N5UXWQCY=3", "陆游51PJ61S2=3", "陈云D0L52TSN=8", "陈公博5GFBRA98=4",
            "陈其美C5Z6XYVT=3", "陈嘉庚WPEDSCCX=3", "陈寅恪EL29IVIY=3", "陈景润ALJLZ5A9=3", "陈毅PQ05D331=3", "陈水扁HR28F724=5", "陈济棠EDVQ64BY=4",
            "陈炯明8PYG4RQZ=5", "陈独秀YSKC6G5W=13", "陈百强98GXAXSQ=3", "陈纳德4ENSBTLU=4", "隆美尔ACCUJCYB=4", "隋文帝1M8VMPKL=4", "隋炀帝Q0MXBKHB=5",
            "雅各布·格林VPA5N6R4=3", "雪莱JRGGL45W=3", "霍元甲HEGR74QE=4", "霍克VHP375P8=3", "霍华德BW8A8VIO=3", "霍梅尼1J0XYNVV=8", "霍雷肖·纳尔逊9A2N3PY2=3",
            "韩复榘M50EMK85=4", "顺治帝ZRB37D87=3", "顾圣婴GXTXDPOZ=3", "顾执中XSBPUMCY=3", "顾颉刚5LRKO62B=3", "颜惠庆EZZR3K2K=3", "马丁·路德6H1PMCL7=4",
            "马丁·路德·金5537GF3K=5", "马克·吐温0IX7QIQY=4", "马兰·梅森IM15W09S=3", "马占山X7DF1I01=3", "马叙伦KL5KGVS8=3",
            "马可·波罗5SNTVOJ8=3", "马向东DFOIYU32=4", "马寅初XYSN0QNI=7", "马思聪OCD3VWAX=3", "马拉多纳5LYUYCCG=7", "马斯卡尼MJ4WJS2N=3",
            "马晓春L3PWF76B=3", "马本斋R04C6WRJ=3", "马林科夫NL3ZUGAP=3", "马歇尔RV79U8XH=5", "马科斯5P10W41M=4", "马英九UOW89HNV=3",
            "马雅可夫斯基PJNDRRG8=3", "高仓健ZBYCCQJM=3", "魏征IIDGNH9S=3", "魏忠贤9SERX2F3=3", "鲁本斯NS6FU8EW=3", "鲁迅W3U3TWZT=9",
            "麦克阿瑟PJG9BR81=3", "麦加CFKHLCJI=3", "麦卡锡71W1018T=4", "麦哲伦IQT0PBM9=4", "黄兴WJP5U4RC=5", "黄炎培Q6FG66L2=3",
            "黄飞鸿N5GJB2LA=3", "黑格尔JDLDCNGG=3"));


    @Test
    public void contextLoads() {
    }

    @Test
    public void testCrawlerUtil() throws Exception {
        System.out.println(rawinfoController.findRawInfo("曹操"));
    }

/*    @Test
    public void testDel() throws UnsupportedEncodingException {
        String ss = "曹操T3HRGAS9%%曹操";
        paramController.deleteParam(ss);
    }*/

/*    @Test
    public void findById() throws Exception {
        String objectId = "曹操T3HRGAS9";
        hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        System.out.println("=================================================================================");
        System.out.println(result);
    }

    @Test
    public void deleteEventByEventId() {
        String eventId = "94be7609-5a60-4a14-923e-631d440aefb1";
        String eventId2 = "f1f05246-05b3-45c8-bafc-d2c65309ef62";

        eventRepository.delete(eventId);
        System.out.println("=================================================================================");
    }

    @Test
    public void insertObject() {
        hbaseDao.insertData("Object", "2", "Eventlist", "0202", "event2", null);
    }

    @Test
    public void getEventList() throws InterruptedException {
        String rawInfo = "0184，黄巾起义爆发，曹操成为骑都尉";
        extractionController.getExtractResult(rawInfo);
    }

    @Test
    public void deleteRele() {
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "description");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "sourceEntityId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "sourceEventId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "targetEntityId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "targetEventId");
    }*/

/*    @Test
    public void findNodeNum() throws Exception {
        //查询每个对象下的事件节点数。
        List<Result> rets = hbaseDao.findAll("Object");
        ArrayList<Map<String, Integer>> returns = new ArrayList<>();
        ArrayList<String> objects = new ArrayList<>();
        int i = 0;
        for (Result result : rets) {
            String objectId = Bytes.toString(result.getRow());
            RealEntity realEntity = new RealEntity();
            realEntity.setObjectId(objectId);
            for (KeyValue kv : result.list()) {
                HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
                realEntity = objectServiceImpl.packageModel(realEntity, hbaseModel);
            }
            ArrayList<Event> events = realEntity.getEvents();

            if (events.size() > 2) {
                if (i > 10) {
                    break;
                }
                i++;
                System.out.println(events);
                System.out.println(events.get(0));
                Map<String, Integer> map = new HashMap<>();
                map.put(objectId, events.size());
                returns.add(map);
                objects.add(objectId);
            }
        }
        System.out.println("=========================================");
        System.out.println(objects);
        System.out.println("=========================================");
        System.out.println(returns);
        System.out.println("=========================================");
        System.out.println(returns.size());
        System.out.println("=========================================");
    }

    @Test
    public void findObject() throws Exception {
        list = new ArrayList<>(Arrays.asList("马丁·路德·金5537GF3K=5", "马克·吐温0IX7QIQY=4", "马兰·梅森IM15W09S=3", "马克·吐温0IX7QIQY=4"));
        ArrayList<String> nameList = new ArrayList<>();
        for (String name : list) {
            String[] strings = name.split("=");
            name = strings[0];
            // name = name.substring(0, name.length() - 8);
            nameList.add(name);
        }
        String tableName = "Object";
        ArrayList<RealEntity> results = new ArrayList<>();
        ArrayList<NewPerson> people = new ArrayList<>();
        //ArrayList<String> nameList = new ArrayList<>();
        for (String name : nameList) {

            RealEntity realEntity = objectService.findEntityById(name);
            results.add(realEntity);
            System.out.println(realEntity.getEvents());

        }
    }*/


/*    @Test
    public void buildPerson() throws IOException {
        String tableName = HbaseModelUtil.BASIC_TABLE;
        List<Result> rets = hbaseDao.findAll(tableName);
        List<NewPerson> people = new ArrayList<>();
        List<String> jsons = new ArrayList<>();
        int i = 0;
        for (Result result : rets) {
            String objectId = Bytes.toString(result.getRow());
            RealEntity realEntity = new RealEntity();
            realEntity.setObjectId(objectId);
            for (KeyValue kv : result.list()) {
                HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
                realEntity = objectServiceImpl.packageModel(realEntity, hbaseModel);
            }
            ArrayList<Event> events = realEntity.getEvents();
            //如果事件节点数大于2个
            if (events.size() > 2) {
                //new 一个NewPerson对象
                NewPerson newPerson = new NewPerson();
                //生成一个ObjectId
                String _id = "{\"$oid\":" + new ObjectId().toHexString() + "}";
                newPerson.set_id(_id);
                String[] intros = realEntity.getRealName().split(" ");
                if (intros.length >= 2) {
                    //realEntity的格式为：名字+简介
                    //如：《中国青年》 一份报刊
                    newPerson.setFigureName(intros[0]);
                    newPerson.setIntro(intros[1]);
                } else {
                    newPerson.setFigureName(intros[0]);
                    newPerson.setIntro("");
                }
                ArrayList<String> list = new ArrayList<>();
                for (Event event : events) {
                    list.add(event.getEventId());
                }
                newPerson.setEvents(list);
                String json = JSON.toJSONString(newPerson);
                jsons.add(json);
                people.add(newPerson);
            }
        }
        //创建输出缓冲流对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("newPerson.txt"));
        //遍历集合，得到每一个字符串元素，然后把该字符串元素作为数据写到文本文件
        for (String string : jsons) {
            bw.write(string);
            bw.newLine();
            bw.flush();
        }
        //释放资源
        bw.close();
        System.out.println(people);
        System.out.println(jsons);
    }

    @Test
    public void buileEvent() throws IOException {
        File file = new File("1array.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;
        int lineCount = 1;
        List<NewPerson> people = new ArrayList<>();
        List<NewEvent> events = new ArrayList<>();
        List<String> jsons = new ArrayList<>();
        //读文件
        while (null != (strLine = bufferedReader.readLine())) {
            //System.out.println(lineCount+":"+strLine);
            NewPerson person = JSONObject.parseObject(strLine, NewPerson.class);
            for (String eventId : person.getEvents()) {
                Event event = eventRepository.queryEventByEventId(eventId);
                NewEvent newEvent = new NewEvent();
                newEvent.set_id(eventId);
                newEvent.setDate(event.getTs());
                newEvent.setEventDetail(event.getDetails());
                newEvent.setEventIntro("");
                newEvent.setObj_id(person.get_id());
                newEvent.setPName(new ArrayList<>(Arrays.asList(person.getFigureName())));
                newEvent.setSName(new ArrayList<>(Arrays.asList(event.getSite())));

                //将newEvent转为json
                String json = JSON.toJSONString(newEvent);
                jsons.add(json);
                events.add(newEvent);

            }
            people.add(person);

            lineCount++;
        }

        //写文件
        //创建输出缓冲流对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("newEvent.txt"));
        //遍历集合，得到每一个字符串元素，然后把该字符串元素作为数据写到文本文件
        for (String string : jsons) {
            bw.write(string);
            bw.newLine();
            bw.flush();
        }
        //释放资源
        bw.close();
    }*/

/*    @Test
    public void outPerson() throws IOException {
        //输出符合标准的newEvent 和newPerson
        File file = new File("newPerson.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;

        //处理得到只有名字的列表
        ArrayList<String> nameList = new ArrayList<>();
        for (String name : list) {
            String[] strings = name.split("=");
            name = strings[0];
            name = name.substring(0, name.length() - 8);
            nameList.add(name);
        }
        System.out.println(nameList);

        List<NewPerson> people = new ArrayList<>();
        List<NewEvent> events = new ArrayList<>();
        List<String> jsons = new ArrayList<>();
        //读文件
        while (null != (strLine = bufferedReader.readLine())) {
            //System.out.println(lineCount+":"+strLine);
            NewPerson person = JSONObject.parseObject(strLine, NewPerson.class);
            if (nameList.contains(person.getFigureName())) {
                people.add(person);
                String json = JSON.toJSONString(person);
                System.out.println(json);
                jsons.add(json);
            }
        }

        //写文件
        //创建输出缓冲流对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("person.txt"));
        //遍历集合，得到每一个字符串元素，然后把该字符串元素作为数据写到文本文件
        for (String string : jsons) {
            bw.write(string);
            bw.newLine();
            bw.flush();
        }
        //释放资源
        bw.close();
    }

    @Test
    //输出有时间线的人物所包含的event
    public void outEvent() throws IOException {
        //输出符合标准的newEvent 和newPerson
        File file = new File("newEvent.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;

        //处理得到只有名字的列表
        ArrayList<String> nameList = new ArrayList<>();
        for (String name : list) {
            String[] strings = name.split("=");
            name = strings[0];
            name = name.substring(0, name.length() - 8);
            nameList.add(name);
        }
        //System.out.println(nameList);

        List<NewPerson> people = new ArrayList<>();
        List<NewEvent> events = new ArrayList<>();
        List<String> jsons = new ArrayList<>();
        //读文件
        while (null != (strLine = bufferedReader.readLine())) {
            //System.out.println(lineCount+":"+strLine);
            NewEvent event = JSONObject.parseObject(strLine, NewEvent.class);
            if (nameList.contains(event.getPName().get(0))) {
                events.add(event);
                String json = JSON.toJSONString(event);
                System.out.println(json);
                jsons.add(json);
            }
        }

        //写文件
        //创建输出缓冲流对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("event.txt"));
        //遍历集合，得到每一个字符串元素，然后把该字符串元素作为数据写到文本文件
        for (String string : jsons) {
            bw.write(string);
            bw.newLine();
            bw.flush();
        }
        //释放资源
        bw.close();

    }*/

}
