function getProjLst() {

    $.ajax({
        type: "get",
        url: "/getmyprojlst",
        success: function (lst) {
            if(lst==null){
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '请先登录',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/myproject";
                    }
                });
            }
            initProjTable(lst);
        }

    });
}

function initProjTable(lst) {
    $('#myproject-table').bootstrapTable({
        toolbar: '#project-toolbar',
        pageSize: 5,
        pageList: [5, 10, 15],
        pagination: 'true',
        search: 'true',
        columns: [{
            field: 'name',
            align: 'center',
            title: '项目名'
        }, {
            field: 'createrId',
            align: 'center',
            title: '负责人',
            formatter: getcreater
        }, {
            field: 'type',
            align: 'center',
            title: '类型',
            formatter: typeparse
        }, {
            field: 'status',
            align: 'center',
            title: '状态',
            formatter: statusparse
        }, {
            align: 'center',
            title: '操作',
            formatter: operatorbtn
        }],
        data: lst
    });
}

self.getProjLst();
let creaters = [];
$.ajax({
    type: 'get',
    async: false,
    url: '/getCreater',
    success: function (data) {
        creaters = data;
    }
});

function getcreater(value, row, index) {
    return creaters[row.createrId.toString()];
}

function typeparse(value, row, index) {
    if (row.type == 'sm') return '小型';
    else if (row.type == 'md') return '中型';
    else return '大型';
}

function statusparse(value, row, index) {
    if (row.status == 0) return '未启动';
    else if (row.status == 1) return '未完成';
    else return '已完成';
}

function operatorbtn(value, row, index) {
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >详细</button>' +
        '<button class="btn btn-sm btn-success mar-rgt" onclick="turnAlter(' + row.id + ')" >修改</button>' + forced;
}
