function getmemberlst() {
    $.ajax({
        type:'get',
        url:'/getprojname',
        success:function (projname) {
            $('#proj-name').text(projname);
        }
    });
    $.ajax({
        type:'get',
        url:'/getmemberlst',
        dataType:'json',
        success:function (lst) {
            initmembertable(lst);
        }
    });
}
function initmembertable(lst) {
    $('#member-table').bootstrapTable({
        toolbar: '#project-toolbar',
        pageSize: 5,
        pageList: [5, 10, 15],
        pagination: 'true',
        search: 'true',
        columns: [{
            field: 'name',
            align: 'center',
            title: '姓名'
        }, {
            field: 'skill',
            align: 'center',
            title: '技能',
        }, {
            field: 'experience',
            align: 'center',
            title: '经验(年)',
        }, {
            align: 'center',
            title: '操作',
            formatter:opt
        }],
        data: lst
    });
}
function opt(){
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >详细</button>' +
        '<button class="btn btn-sm btn-success mar-rgt" onclick="turnAlter(' + row.id + ')" >修改</button>' + forced;
}
window.onload=function () {
    getmemberlst();
};