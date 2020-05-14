// 监听coco组件完成初始化 
window.addEventListener("message", function (e) {
    var d = e.data
    console.log("在html触发了" + d.method)
    if (d.origin !== 'ochart-iframe') return;
    if (d.method == 'data_from_android') return;// 初始化的数据就不走这里了
    window.Android.eventFucntion(d.method,d.data?JSON.stringify(d.data):"");
  })
  // 接收安卓发来的数据
  function showInfoFromJava(param) {
    let jsonStr = decode(param)
    console.log("解码后的长度===" + jsonStr.length)
    let obj = JSON.parse(jsonStr)
    // 发送初始化数据
    window.postMessage({
      origin: 'ochart-iframe',
      method: "data_from_android",
      data: obj
    }, "*");
  }

  // base64转字符串
  function decode(base64) {
    // 对base64转编码
    var decode = atob(base64);
    // 编码转字符串
    var str = decodeURIComponent(decode);
    return str;
  }