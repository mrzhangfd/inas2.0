var flag2 = true;
var myChart = echarts.init(document.getElementById('select_area'));
// var webkitDep = {
//     categories:[{name: "核心词"},       //关系网类别
//         {name: "属性组"}],
//     nodes:[{category: 0, name: "曹操",symbolSize: 40, value: 5, id: 0},  //展示的节点 //category与关系网类别索引对应 //我的源数据中没有id属性，这里放出来的是目标数据，id是自动生成的
//         {category: 1, name: "朝代: 东汉", symbolSize: 20,value: 3, id: 1},
//         {category: 1, name: "民族: 汉族", symbolSize: 20,value: 1, id: 2},
//         {category: 1, name: "官职: 丞相", symbolSize: 20,value: 1, id: 3},
//         {category: 1, name: "出生地: 安徽亳州", symbolSize: 20,value: 1, id: 4},
//         {category: 1, name: "字号: 孟德", symbolSize: 20,value: 1, id: 5}
//     ],
//
//
//     links:[{source: 0, target: 1, value: 5},    //节点之间连接 //source起始节点，0表示第一个节点  //target目标节点，1表示与索引(id)为1的节点进行连接
//         {source: 0, target: 2, value: 3},
//         {source: 0, target: 3, value: 3},
//         {source: 0, target: 4, value: 3},
//         {source: 0, target: 5, value: 3}
//     ]
// }
//
// var option = {
//     legend: {
//         x: 'left',//图例位置
//         //图例的名称
//         //此处的数据必须和关系网类别中name相对应
//         data: webkitDep.categories.map(function (a) {
//             return a.name;
//         })
//     },
//     series: [{
//         type: 'graph',
//         layout: 'force',
//         // animation: false,
//         label: {
//             normal: {
//                 show:true,
//                 position: 'right'
//             }
//         },
//         draggable: true,
//         force: {
//             layoutAnimation:true,
//             // xAxisIndex : 0, //x轴坐标 有多种坐标系轴坐标选项
//             // yAxisIndex : 0, //y轴坐标
//             gravity:0.03,  //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
//             edgeLength: 55,  //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
//             repulsion: 150  //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
//         },
//         data: webkitDep.nodes.map(function (node, idx) {  //node数据
//             node.id = idx;
//             return node;
//         }),
//         categories: webkitDep.categories,  //关系网类别，可以写多组
//         edges: webkitDep.links  //link数据
//     }]
// };
// myChart.setOption(option);//将option添加到mychart中

$(function () {
    $("#add").click(function () {
        if (flag2){
            document.getElementById("newparam").style.display= "block";
            document.getElementById("add").innerHTML= "pack";
            flag2 = false;
        }else {
            document.getElementById("newparam").style.display= "none";
            document.getElementById("add").innerHTML= "Add New Timenode";
            flag2 = true;
        }
    })

});

window.onload = function() {
    $.ajax({
        type: "post",
        url: "/param.do",
        data: {objectId: $('#objectId').val()},
        dataType: "json",
        success: function (result) {
            myChart.setOption({
                legend: {
                    x: 'center',//图例位置
                    //图例的名称
                    //此处的数据必须和关系网类别中name相对应
                    z:"bottom",
                    data: result.categories.map(function (a) {
                        return a.name;
                    })
                },

                series: [
                    {
                        type: 'graph',
                        layout: 'force',
                        label: {
                            normal: {
                                show:true,
                                position: 'right'
                            }
                        },

                        draggable: true,
                        // progressiveThreshold: 700,
                        force: {
                            layoutAnimation:true,
                            // xAxisIndex : 0, //x轴坐标 有多种坐标系轴坐标选项
                            // yAxisIndex : 0, //y轴坐标
                            gravity:0.03,  //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
                            edgeLength: 55,  //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
                            repulsion: 150  //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
                        },
                        data: result.nodes.map(function (node) {
                            return {
                                category: node.serial,
                                id: node.id,
                                name: node.label,
                                symbolSize: node.size
                                // itemStyle: {
                                //     normal: {
                                //         color: node.color
                                //     }
                                // }
                            };
                        }),
                        edges: result.edges.map(function (edge) {
                            return {
                                source: edge.sourceID,
                                target: edge.targetID
                            };
                        }),
                        roam: true,
                        focusNodeAdjacency: true,
                        categories: result.categories
                    }
                ]
            }, true);
            // return false;
        }
    });
};