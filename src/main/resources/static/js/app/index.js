var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
        $('#btn-update').on('click', function () {
            _this.update();
        });
        $('#btn-delete').on('click', function () {
            _this.delete();
        });
        $('#btn-attachment-delete').on('click', function (e) {
            _this.deleteAttachment(e.target.value);
        })
    },
    save : function () {
        var form = new FormData();
        form.append("title", $('#title').val())
        form.append("content", $('#content').val())
        form.append("attachment", $('#attachment')[0].files[0])

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            processData: false,
            contentType: false,
            data: form,
            dataType: 'text'
        }).done(function (){
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (){
            alert(JSON.stringify(error.responseJSON.message));
        });
    },
    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error.responseJSON.message));
        });
    },
    delete: function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset:utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href='/';
        }).fail(function (error){
            alert(JSON.stringify(error.responseJSON.message));
        });
    },
    deleteAttachment: function (attachmentId) {

        if(!window.confirm("파일을 정말로 삭제하시겠습니까?")) return;

        $.ajax({
            type: 'DELETE',
            url: '/attachment/' + attachmentId,
            dataType: 'json',
            contentType: 'application/json; charset:utf-8'
        }).done(function() {
            $(`#attachment-${attachmentId}`).remove();
            alert('첨부파일이 삭제되었습니다.');
        }).fail(function (error){
            alert(JSON.stringify(error.responseJSON.message));
        });
    }
};

main.init();