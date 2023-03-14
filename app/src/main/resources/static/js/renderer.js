//const drop = document.getElementById("drop");
const dropdiv = document.getElementById("drop");
const picurl = document.getElementById("urltext");

const pic = document.getElementById("pic");
const chartdiv = document.getElementById("chart");

var itemindex = 0;

dropdiv.appendChild(additem("pic/testPic.jpg"));



//-------- file_drog ---------//
dropdiv.ondragover = (ev) => ev.preventDefault();
dropdiv.ondrop = (ev) => {
  ev.preventDefault();
  let url = ev.dataTransfer.getData("url");
  // picurl.innerHTML = url;

  dropdiv.appendChild(additem(url));


}
///------ end file drog ---------//

const additem = (url) => {
  itemindex += 1;
  let itemid = "item" + itemindex
  let item = document.createElement("div");
  item.setAttribute("id", itemid);
  item.setAttribute("class", "row");
  item.append(addPic(url));
  //item.append(addPic(url));
  item.append(addChart(url));
  return item;

}

const addPic = (url) => {
  let piccol = document.createElement("div");
  picid = "pic" + itemindex;
  piccol.setAttribute("id", picid);
  piccol.setAttribute("class", "col");


  let img = document.createElement("img");
  img.setAttribute("src", url);
  img.setAttribute("class", "img-fluid");
  img.setAttribute("width", 300);
  img.setAttribute("height", 300);
  // img.setAttribute("height", 200);
  // img.setAttribute("width", 200);
  piccol.append(img);
  return piccol;
}



const addChart = (url) => {
  let chartscol = document.createElement("div");
  chartid = "chart" + itemindex;
  chartscol.setAttribute("id", chartid);
  chartscol.setAttribute("class", "col");

  let IRurl = "/IR?picUrl=" + url;
  //alert(IRurl)
  fetch(IRurl)
    .then((response) => response.text())
    .then((data) => {
      setEcharts(data, chartid);
    })
    .catch((error) => {
      console.warn(error);
    });

  return chartscol;
}

const setEcharts = (data, chartid) => {
  let chartdiv = document.getElementById(chartid);
  //console.log(data);
  // [{"classname":"n06596364 comic book","value":0.35678377747535706}
  // ,{"classname":"n04536866 violin, fiddle","value":0.1291659027338028}
  // ,{"classname":"n04277352 spindle","value":0.07362811267375946}]
  let jsondata = JSON.parse(data)
  let piedata = []
  console.log(jsondata)
  let ortherValue = 1.0;
  jsondata.forEach(e => {
    let o = {}

    o.name = e.classname.substring(10)

    o.value = e.value.toFixed(2)
    ortherValue -= o.value
    piedata.push(o);
  });
  let orther = {}
  orther.name = "其他"
  orther.value = ortherValue.toFixed(2)
  piedata.push(orther);
  // piedata.forEach(e => {
  //   console.log(e.value)
  // })


  const myChart = echarts.init(chartdiv
    , null
    , {
      width: 600,
      height: 300,
    }
  );
  window.onresize = function () {
    myChart.resize();
  }

  option = {
    // legend: {
    //   orient: 'vertical',
    //   //backgroundColor: '#ccc',
    //   left: 1,
    //   // top: 20,
    //   //bottom: 20,
    // },
    series: [

      {
        // center: ['65%', '35%'],
        //roseType: 'area', //南丁格尔图
        // label: {
        //   show: false,
        //   normal: {
        //     show: true,
        //     position: 'inner',
        //     formatter: '{d}%'
        //   }
        // },
        type: 'pie',
        data: piedata
      }
    ]
  };
  myChart.setOption(option);

}


