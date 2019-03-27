function validarFormulario(form) {
    var inputs = form.find(".js-obrigatorio2");

    //busca o valor de todos inputs e verifica se algum possui valor nulo ou em branco
    inputs.each(function(index, input) {
        if ($(input).val() == null || $(input).val() == "") {
            $(input).addClass("is-invalid");
            $(input).removeClass("is-valid");
        } else {
            $(input).addClass("is-valid");
            $(input).removeClass("is-invalid");
        }
    });

    //se nao possuir nenhum input com a classe is-invalid o formulario é submetido
    if (!$(".is-invalid").length > 0) {
        form.submit();
    }
}

function carregarOptionsCurso(selectContainer, cursoId){
    selectContainer.empty();

    var resposta = $.ajax({
        headers:
        { '_csrf': $('#token').val()},
        url: '/rest/turmas/?cursoId='+cursoId,
        method: 'GET',
        scriptCharset: "utf-8",
    });

    resposta.done(function(data) {
        $.each( data, function( key, turma ) {
            selectContainer.append(`<option value="`+turma.id+`">`+turma.semestre+` - `+turma.letra+` (`+turma.ano+`)</option>`)
        });
    });

    resposta.fail(function(data) {
        alert('Falha ao buscar as turmas');
    });

}


$( document ).ready(function() {

    $(".js-link-upload-foto").click(function() {
         var form = $("#drag-and-drop-zone");
         form.data('img-id', $(this).data('img-id'));
         form.data('input-result-id', $(this).data('input-result-id'));
         form.find('input').click();
     });

    $( ".js-btn-finalizar" ).click(function() {
        var form = $(this).closest("form");
        //validarFormulario(form);
        form.submit();
    });

    $(".js-cursos").change(function() {
        var cursoId = $(this).val();
        var select = $('.js-turmas');
        carregarOptionsCurso(select, cursoId);
    });

    //carrega turmas se curso ja estiver selecionar na hora de carregar a pagina

    var cursoId = $(".js-cursos").val();
    var turmaId = $('.js-turmas').val();
    if (cursoId != null && cursoId != '' && turmaId == null) {
        carregarOptionsCurso($('.js-turmas'), cursoId);
    }


    var indexAluno = $('#sec-integrantes').data('quantia');
    var containerAlunos = $('#sec-integrantes');
    var btnAddAluno = $('#btn-add-aluno');

    $( ".js-clica-remove-card" ).click(function() {


        //$(this).closest( ".card" ).remove();


        var form = $('#grupoForm');
        var oldAction = form.attr('action');
        var id = $(this).attr('data-alunoId')
        var newAction = '/grupos/remover-aluno/'+id
        form.attr('action', newAction);
        form.submit();
        form.attr('action', oldAction);


    });

    btnAddAluno.click(function() {

        var form = $(this).closest("form");
        var oldAction = form.attr('action');
        var newAction = '/grupos/adicionar-aluno/'
        form.attr('action', newAction);
        form.submit();
        form.attr('action', oldAction);


    });

 /*   btnAddAluno.click(function() {
        indexAluno = indexAluno + 1;
        var integranteNumero = indexAluno +1
        var corpoHtml =	`<div id="card-0" class="card profile">
                            <div class="actions">
                                    <a  class="actions__item zmdi zmdi-close js-clica-remove-card"></a>
                            </div>
                            <div class="profile__img">
                                <img id="img-foto-integrante-`+indexAluno+`" src="/img/group_empty.jpg" alt="" style="height: 200px;width: 200px;" />
                                <a data-img-id="img-foto-integrante-`+indexAluno+`" data-input-result-id="input-foto-hash-`+indexAluno+`" class="js-link-upload-foto zmdi zmdi-camera profile__img__edit"></a>
                                <input id="input-foto-hash-`+indexAluno+`" name="fotoHash" type="text" hidden=""/>
                            </div>
                            <div class="profile__info">
                                <h3 class="card-body__title">`+integranteNumero+`º Integrante</h3>
                                <div class="col-sm-12">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <input name="alunos[`+indexAluno+`].nome" type="text" class="form-control js-obrigatorio" placeholder="Nome"/>
                                                <div class="invalid-feedback">
                                                    Nome do integrante é obrigatório.
                                                </div>
                                                <i class="form-group__bar"></i>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <input name="alunos[`+indexAluno+`].ra" type="number" class="form-control js-obrigatorio" placeholder="RA">
                                                <div class="invalid-feedback">
                                                    RA é obrigatório.
                                                </div>
                                                <i class="form-group__bar"></i>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <input name="alunos[`+indexAluno+`].email" type="email" class="form-control js-obrigatorio" placeholder="Email">
                                                <div class="invalid-feedback">
                                                    Email é obrigatório.
                                                </div>
                                                <i class="form-group__bar"></i>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <input name="alunos[`+indexAluno+`].telefone" type="number" class="form-control" placeholder="Telefone">
                                                <i class="form-group__bar"></i>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>`

        containerAlunos.append(corpoHtml);

        $( ".js-clica-remove-card" ).click(function() {
            if (indexAluno > 0) {
                //indexAluno = indexAluno - 1;
            }

            $(this).closest( ".card" ).remove();
        });

        $(".js-link-upload-foto").click(function() {
             var form = $("#drag-and-drop-zone");
             form.data('img-id', $(this).data('img-id'));
             form.data('input-result-id', $(this).data('input-result-id'));
             form.find('input').click();
         });

    });
*/
});








$(function(){
  /*
   * For the sake keeping the code clean and the examples simple this file
   * contains only the plugin configuration & callbacks.
   *
   * UI functions //ui_* can be located in:
   *   - assets/demo/uploader/js/ui-main.js
   *   - assets/demo/uploader/js/ui-multiple.js
   *   - assets/demo/uploader/js/ui-single.js
   */
  $('#drag-and-drop-zone').dmUploader({ //
    url: '/publico/arquivos',
    maxFileSize: 3000000, // 3 Megs max
    multiple: false,
    allowedTypes: 'image/*',
    extFilter: ['jpg','jpeg','png','gif'],
    headers:{
        '_csrf': $('#token').val()
    },
    extraData: {
       '_csrf': $('#token').val()
    },
    onDragEnter: function(){
      // Happens when dragging something over the DnD area
      this.addClass('active');
    },
    onDragLeave: function(){
      // Happens when dragging something OUT of the DnD area
      this.removeClass('active');
    },
    onInit: function(){
      // Plugin is ready to use
      console.log('Penguin initialized :)', 'info');

      this.find('input[type="text"]').val('');
    },
    onComplete: function(){
      // All files in the queue are processed (success or error)
      console.log('All pending tranfers finished');
    },
    onNewFile: function(id, file){
    console.log('data-img-id '+this.data('img-id'));
      // When a new file is added using the file selector or the DnD area
      console.log('New file added #' + id);

      if (typeof FileReader !== "undefined"){
        var reader = new FileReader();
        //var img = this.find('img');

        var img = document.getElementById(this.data('img-id'));

        reader.onload = function (e) {
          //adicionando a imagem

          //img.attr('src', e.target.result);
          img.src = e.target.result;
        }
        reader.readAsDataURL(file);
      }
    },
    onBeforeUpload: function(id){
      // about tho start uploading a file
      console.log('Starting the upload of #' + id);
      //ui_single_update_progress(this, 0, true);
      //ui_single_update_active(this, true);

      //ui_single_update_status(this, 'Uploading...');
    },
    onUploadProgress: function(id, percent){
      // Updating file progress
      //ui_single_update_progress(this, percent);
    },
    onUploadSuccess: function(id, data){

      // A file was successfully uploaded
      console.log('Server Response for file #' + id + ': ' + data);
      console.log('Upload of file #' + id + ' COMPLETED', 'success');

      //ui_single_update_active(this, false);

      // You should probably do something with the response data, we just show it
      var inputHash = document.getElementById(this.data('input-result-id'));
      inputHash.value = data
      //this.find('input[type="text"]').val(response);

      //ui_single_update_status(this, 'Upload completed.', 'success');
    },
    onUploadError: function(id, xhr, status, message){
      // Happens when an upload error happens
      //ui_single_update_active(this, false);
      //ui_single_update_status(this, 'Error: ' + message, 'danger');

    },
    onFallbackMode: function(){
      // When the browser doesn't support this plugin :(
      console.log('Plugin cant be used here, running Fallback callback', 'danger');
    },
    onFileSizeError: function(file){
      //ui_single_update_status(this, 'File excess the size limit', 'danger');

      console.log('File \'' + file.name + '\' cannot be added: size excess limit', 'danger');
    },
    onFileTypeError: function(file){
      //ui_single_update_status(this, 'File type is not an image', 'danger');

      console.log('File \'' + file.name + '\' cannot be added: must be an image (type error)', 'danger');
    },
    onFileExtError: function(file){
      //ui_single_update_status(this, 'File extension not allowed', 'danger');

      console.log('File \'' + file.name + '\' cannot be added: must be an image (extension error)', 'danger');
    }
  });
});