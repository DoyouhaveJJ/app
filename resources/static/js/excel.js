var colunm1 = {
    'ID':'ID','描述':'描述','人数':'人数','进度':'进度','项目名':'项目名',
    '质量':'质量','主题':'主题','更新时间':'更新时间','负责人':'负责人'
};



function getOptions() {
    var type = $('#select-type-2').val();
    var colunm;
    switch (type) {
        case '项目报告表': colunm = colunm1; break;

    }
    for (var key in colunm) {
        var option = '<option value="' + colunm[key] + '">' + key + '</option>';
        $('#select-colunm-1').append(option);
    }
};
function initSelectcolunm() {
    getOptions();
    $('#select-colunm-1').chosen({ width: '100%' });
};
self.initSelectcolunm();

$('#select-type-2').on('change', function () {
    var select = '<select id="select-colunm-1" multiple title="选择需要的列" data-width="100%" data-placeholder="请选择需要的列">' +
        '</select>' + '<hr />';
    $('#select-colunm').empty();
    $('#select-colunm').append(select);
    getOptions();
    $('#select-colunm-1').chosen({ width: '100%' });
});

function down() {
    var type = $('#select-type-2').val();
    var value = $('#select-colunm-1').val();
    let ids=value;
    console.log('type:'+type);
    console.log('value:'+value);
    let column={
        value:value
    };
    console.log(JSON.stringify(column));
    $.ajax({
        url: '/getExcel',
        contentType: 'application/json;charset=UTF-8',
        async:false,
        type: 'post',
        data:JSON.stringify(column) ,
        success: function (context) {
            if (context==='success') {
                $.niftyNoty({
                    type: "success",
                    container: "floating",
                    title: "导出成功",
                    message: '马上开始下载文件',
                    closeBtn: false,
                    timer: 5000
                });

            }
        }
    });
    window.location.href="/download";

}
