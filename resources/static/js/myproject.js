function getProjLst() {

    $.ajax({
        type: "get",
        url: "/getmyprojlst",
        success: function (lst) {
            console.log(lst);
            if(lst==''){
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
    let forced = '<button class="btn btn-sm btn-success mar-rgt" onclick="turnAlter(' + row.id + ')" >修改</button>'+
        '<button class="btn btn-sm btn-success mar-rgt" onclick="teamManage(' + row.id + ')" >成员管理</button>';
    if (row.status == 0) {
        forced += '<button class="btn btn-sm btn-danger mar-rgt" onclick="force_start(' + row.id + ')" >启动</button>';
    } else if (row.status == 1) {
        forced += '<button class="btn btn-sm btn-danger mar-rgt" onclick="force_end(' + row.id + ')" >完成</button>';
    }else {
        forced='';
    }
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
                },{
                    align:'center',
                    title:'操作',
                    formatter:removebtn
                }],
                data:data
            });
            $("#new-btn").on("click",function(){
                turnTeam(id);
            });
        }
    });
    $('#member-modal').modal('show');
}
function removebtn(value,row,index) {
    return '<button class="btn btn-sm btn-danger mar-rgt" onclick="removemember('+row.id+')">移除成员</button>';
}
function removemember(id) {
    let member = {
        "id": id
    };
    $.ajax({
        type: 'post',
        url: '/remove',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(member),
        success: function (data) {
            if(data==='success'){
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '移除成功',
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
                    title: '移除失败',
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
function turnTeam(id){
    window.location.href="/team?id="+id;
}
window.onload=function () {
    getProjLst();
};
function turnAlter(id) {
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
            $('#alter-name').val(data.name);
            $('#alter-desc').val(data.description);
            let arr = data.tech.split(';');
            console.log(arr);
            for (let x of arr) {
                console.log(x);
                if (x == 'c/c++') $('#alter-tech-1').attr("checked", true);
                else if (x == 'java') $('#alter-tech-2').attr("checked", true);
                else if (x == '.net') $('#alter-tech-3').attr("checked", true);
                else if (x == 'python') $('#alter-tech-4').attr('checked', true);
            }
            if (data.type == 'sm') $('#alter-type-1').attr('checked', true);
            else if (data.type == 'md') $('#alter-type-2').attr('checked', true);
            else if (data.type == 'lg') $('#alter-type-3').attr('checked', true);
            $('#alter-id').val(id);
        }
    });
    $('#alter-modal').modal('show');
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
function addProj() {
    let proj = {
        "name": "",
        "type": "",
        "tech": "",
        "description": ""
    };
    let flag = [true,true,true,true];
    proj.name = $('#proj-name').val();
    proj.description = $('#proj-desc').val();
    if (proj.name != "") {
        flag[0] = false;
    }
    if (proj.description != "") {
        flag[1] = false;
    }
    $("input[name='proj-type']:checked").each(function () {
        proj.type = $(this).val();
        flag[2] = false;
    });
    $("input[name='proj-tech']:checked").each(function () {
        proj.tech += $(this).val() + ';';
        flag[3] = false;
    });

    if (!flag[0] && !flag[1] && !flag[2] && !flag[3]) {
        $.ajax({
            type: "POST",
            url: "/addNewProj",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(proj),
            success: function (data) {
                console.log(data);
                if (data ==="success") {
                    $.niftyNoty({
                        type: 'success',
                        container: 'floating',
                        title: '新建成功',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.href="/project";
                        }
                    });
                } else if(data==="failure") {
                    $.niftyNoty({
                        type: 'danger',
                        container: 'floating',
                        title: '新建失败,存在同名项目',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.href="/project";
                        }
                    });
                }
            }
        });
    } else {
        $.niftyNoty({
            type: 'danger',
            container: 'floating',
            title: '新建失败',
            message: '',
            closeBtn: true,
            timer: 1000,
            onHidden:function () {
                window.location.href="/project";
            }
        });
    }

}
function alterProj() {
    let proj = {
        "id":-1,
        "name": "",
        "type": "",
        "tech": "",
        "description": ""
    };
    proj.id=$('#alter-id').val();
    let flag = [true,true,true,true];
    proj.name = $('#alter-name').val();
    proj.description = $('#alter-desc').val();
    if (proj.name != "") {
        flag[0] = false;
    }
    if (proj.description != "") {
        flag[1] = false;
    }
    $("input[name='alter-type']:checked").each(function () {
        proj.type = $(this).val();
        flag[2] = false;
    });
    $("input[name='alter-tech']:checked").each(function () {
        proj.tech += $(this).val() + ';';
        flag[3] = false;
    });

    if (!flag[0] && !flag[1] && !flag[2] && !flag[3]) {
        $.ajax({
            type: "POST",
            url: "/alterProj",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(proj),
            success: function (data) {
                console.log(data);
                if (data ==="success") {
                    $.niftyNoty({
                        type: 'success',
                        container: 'floating',
                        title: '修改成功',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.href="/myproject";
                        }
                    });
                } else if(data==="failure") {
                    $.niftyNoty({
                        type: 'danger',
                        container: 'floating',
                        title: '修改失败',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.href="/myproject";
                        }
                    });
                }
            }
        });
    } else {
        $.niftyNoty({
            type: 'danger',
            container: 'floating',
            title: '修改失败',
            message: '',
            closeBtn: true,
            timer: 1000,
            onHidden:function () {
                window.location.href="/myproject";
            }
        });
    }
}
function force_start(id) {
    let proj = {
        "id": id
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        url: '/forcestart',
        data: JSON.stringify(proj),
        success: function (data) {
            if (data == 'success') {
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '启动成功',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/myproject";
                    }
                });
            } else {
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '启动失败',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/myproject";
                    }
                });
            }
        }
    });
}

function force_end(id) {
    let proj = {
        "id": id
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        url: '/forceend',
        data: JSON.stringify(proj),
        success: function (data) {
            if (data == 'success') {
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '完成成功',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/myproject";
                    }
                });
            } else {
                $.niftyNoty({
                    type: 'danger',
                    container: 'floating',
                    title: '完成失败',
                    closeBtn: true,
                    timer: 1000,
                    onHidden:function () {
                        window.location.href="/myproject";
                    }
                });
            }
        }
    });
}
function turnAdd() {
    window.location.href="/addproject";
}