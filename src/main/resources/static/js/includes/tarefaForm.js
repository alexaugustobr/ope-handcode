
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
            selectContainer.append(`<option value="`+turma.id+`">`+turma.semestre+` - `+turma.letra+` - `+turma.curso.sigla+` (`+turma.periodo+`)</option>`)
        });
    });

    resposta.fail(function(data) {
        alert('Falha ao buscar as turmas');
    });

}

$( document ).ready(function() {

    $(".js-cursos").change(function() {
        var cursoId = $(this).val();
        var select = $('.js-turmas');
        carregarOptionsCurso(select, cursoId);
    });

});