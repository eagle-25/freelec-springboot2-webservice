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
        $('#btn-add-attachment').on('click', function ( ) {
            _this.addAttachments();
        });
    },
    save : function () {
        let form = new FormData();
        form.append("title", $('#title').val())
        form.append("content", $('#content').val())

        for(let i = 0; i < $('#attachments')[0].children.length; i++)
        {
            var id = $('#attachments')[0].children[i].id.split("-")[2];
            var fileTagId = '#attachment-' + id;

            if($(fileTagId)[0].files.length < 1) continue;

            form.append("attachments", $(fileTagId)[0].files[0]);
        }

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
        }).fail(function (error){
            alert(JSON.stringify(error.responseJSON.message));
        });
    },
    update : function () {
        var form = new FormData();
        form.append("title", $('#title').val())
        form.append("content", $('#content').val())
        form.append("removedAttachments", _removeAttachmentList);

        for(let i = 0; i < $('#attachments')[0].children.length; i++)
        {
            var id = $('#attachments')[0].children[i].id.split("-")[2];
            var fileTagId = '#attachment-' + id;


            if($(fileTagId)[0] == null || $(fileTagId)[0].type != "file") continue;

            form.append("addedAttachments", $(fileTagId)[0].files[0]);
        }

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            processData: false,
            contentType: false,
            data: form,
            dataType: 'text'
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
        _removeAttachmentList.push(attachmentId);
        $(`#attachment-li-${attachmentId}`).remove();
    },
    addAttachments: function () {
        var id = new Date().getTime();

        $(`#attachments`)
            .append(`<li id="attachment-li-${id}\" class=\"row\">
                        <div class=\"col-md-9\">
                            <input type=\"file\" id=\"attachment-${id}\" class=\"form-control\"  placeholder=\"파일 선택\" formenctype=\"multipart/form-data\">
                        </div>
                        <div class=\"row-md-3\">
                            <button type=\"button\" id=\"btn-attachment-delete\" value=\"${id}\" onclick="main.deleteAttachment(${id})" class=\"btn btn-danger\">삭제</button>
                        </div>
                    </li>`);
    }
};

var _removeAttachmentList = new Array();

main.init();