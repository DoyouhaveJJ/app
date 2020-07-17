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
    let enterbtn="";
    let team={
        "projectId":row.id
    };
    $.ajax({
        type:'post',
        contentType: 'application/json;charset=UTF-8',
        async:false,
        url: '/checkmem',
        data: JSON.stringify(team),
        success:function (data) {
            if(data==='yes'){
                enterbtn='<button class="btn btn-sm btn-success mar-rgt" disabled >已加入</button>';
            }else if(data==='no') {
                enterbtn='<button class="btn btn-sm btn-success mar-rgt" onclick="joinin('+row.id+')" >加入</button>';
            }
        }
    });
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >详细</button>' +
        enterbtn;
}
function joinin(id) {
    let proj = {
        "id": id
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        url: '/joinin',
        data: JSON.stringify(proj),
        success: function (data) {
            if(data==='success'){
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '加入成功',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.reload();
                    }
                });
            }else {
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '加入失败',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.reload();
                    }
                });
            }
        }
    });
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

function getProjLst() {

    $.ajax({
        type: "get",
        url: "/getprojlst",
        success: function (lst) {
            initProjTable(lst);
        }

    });
}

function initProjTable(lst) {
    $('#project-table').bootstrapTable({
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