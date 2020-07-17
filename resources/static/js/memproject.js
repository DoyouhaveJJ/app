function getProjLst() {

    $.ajax({
        type: "get",
        url: "/getmyprojlst",
        success: function (lst) {
            console.log(lst);
            if(lst===''){
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '请先登录',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/login";
                    }
                });
            }else if(lst[0].context==='none'){
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '还没有与你有关的项目',
                    closeBtn: true,
                    timer: 1000
                });
            }
            initProjTable(lst);
        }

    });
}

function initProjTable(lst) {
    $('#myproject-table').bootstrapTable({
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
    let forced = '<button class="btn btn-sm btn-success mar-rgt" onclick="teamManage(' + row.id + ')" >成员</button>';
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >详细</button>' +
        forced;
}
function teamManage(id) {
    let proj = {
        "id": id
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        url: '/getmembers',
        data: JSON.stringify(proj),
        success:function (data) {
            $('#member-table').bootstrapTable('destroy');
            console.log(data);
            $('#member-table').bootstrapTable({
                toolbar: '#mem-bar',
                pageSize: 5,
                pageList: [5, 10, 15],
                pagination: 'true',
                search: 'true',
                columns: [{
                    field:'name',
                    align:'center',
                    title:'姓名'
                }],
                data:data
            });
        }
    });
    $('#member-modal').modal('show');
}
function turnCheck(id) {
    let proj = {
        "id": id
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        url: '/getproj',
        data: JSON.stringify(proj),
        success: function (data) {
            console.log(data);
            $('#more-name').text(data.name);
            $('#more-creater').text(creaters[data.createrId.toString()]);
            $('#more-start').text(data.start);
            $('#more-end').text(data.end);
            $('#more-desc').text(data.description);
            $('#more-tech').text(data.tech);
        }
    });
    $('#more-modal').modal('show');
}
window.onload=function () {
    getProjLst();
};