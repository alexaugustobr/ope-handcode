function validarFormulario(form) {

}

function buscarEventos(token, calendario){

    var resposta = $.ajax({
            headers:
            { '_csrf': token},
            url: '/rest/eventos/',
            method: 'GET',
            scriptCharset: "utf-8",
        });

        resposta.done(function(data) {
            $.each( data, function( key, e ) {
                calendario.fullCalendar('renderEvent', {
                    id: e.id,
                    title: e.titulo,
                    start: e.dataHora,
                    end:   e.dataHora,
                    allDay: true,
                    className: 'bg-blue',
                    description: e.descricao,
                    type: e.type
                }, true);
            });
        });
        resposta.fail(function(data) {
            alert('Falha ao buscar os eventos');
        });

}


$( document ).ready(function() {

$(document).ready(function() {
                var date = new Date();
                var m = date.getMonth();
                var y = date.getFullYear();
                var calendario = $('#calendario');

                var token = $('#token').val()

                buscarEventos(token, calendario);

                calendario.fullCalendar({
                    header: {
                        right: '',
                        center: '',
                        left: ''
                    },
                    buttonIcons: {
                        prev: 'calendar__prev',
                        next: 'calendar__next'
                    },
                    theme: false,
                    selectable: true,
                    selectHelper: true,
                    editable: true,
                    events: [],

                    dayClick: function(date) {
                        console.log('date')
                        /* var isoDate = moment(date).toISOString();
                        $('#new-event').modal('show');
                        $('.new-event__title').val('');
                        $('.new-event__start').val(isoDate);
                        $('.new-event__end').val(isoDate); */
                    },

                    viewRender: function (view) {
                        console.log('view')
                        var calendarDate = $('.calendar').fullCalendar('getDate');
                        var calendarMonth = calendarDate.month();

                        //Set data attribute for header. This is used to switch header images using css
                        $('.calendar .fc-toolbar').attr('data-calendar-month', calendarMonth);

                        //Set title in page header
                         $('.content__title--calendar > h1').html(view.title);
                    },

                    eventClick: function (event, element) {
                        console.log(event)
                        if (event.type == 'Tarefa') {
                            window.location = '/painel/aluno/tarefas/'+event.id
                        } else {
                            window.location = '/painel/aluno/eventos/'+event.id
                        }

//                        $('#edit-event input[value='+event.className+']').prop('checked', true);
//                        $('#edit-event').modal('show');
//                        $('.edit-event__id').val(event.id);
//                        $('.edit-event__title').val(event.title);
//                        $('.edit-event__description').val(event.description);
                    }
                });


                //Add new Event
               /*  $('body').on('click', '.new-event__add', function(){
                    var eventTitle = $('.new-event__title').val();

                    // Generate ID
                    var GenRandom =  {
                        Stored: [],
                        Job: function(){
                            var newId = Date.now().toString().substr(6); // or use any method that you want to achieve this string

                            if( !this.Check(newId) ){
                                this.Stored.push(newId);
                                return newId;
                            }
                            return this.Job();
                        },
                        Check: function(id){
                            for( var i = 0; i < this.Stored.length; i++ ){
                                if( this.Stored[i] == id ) return true;
                            }
                            return false;
                        }
                    };

                    if (eventTitle != '') {
                        $('.calendar').fullCalendar('renderEvent', {
                            id: GenRandom.Job(),
                            title: eventTitle,
                            start: $('.new-event__start').val(),
                            end:  $('.new-event__end').val(),
                            allDay: true,
                            className: $('.event-tag input:checked').val()
                        }, true);

                        $('.new-event__form')[0].reset();
                        $('.new-event__title').closest('.form-group').removeClass('has-danger');
                        $('#new-event').modal('hide');
                    }
                    else {
                        $('.new-event__title').closest('.form-group').addClass('has-danger');
                        $('.new-event__title').focus();
                    }
                });
 */

                //Update/Delete an Event
                /*$('body').on('click', '[data-calendar]', function(){
                    var calendarAction = $(this).data('calendar');
                    var currentId = $('.edit-event__id').val();
                    var currentTitle = $('.edit-event__title').val();
                    var currentDesc = $('.edit-event__description').val();
                    var currentClass = $('#edit-event .event-tag input:checked').val();
                    var currentEvent = $('.calendar').fullCalendar('clientEvents', currentId);

                    //Update
                    if(calendarAction === 'update') {
                        if (currentTitle != '') {
                            currentEvent[0].title = currentTitle;
                            currentEvent[0].description = currentDesc;
                            currentEvent[0].className = currentClass;

                            $('.calendar').fullCalendar('updateEvent', currentEvent[0]);
                            $('#edit-event').modal('hide');
                        }
                        else {
                            $('.edit-event__title').closest('.form-group').addClass('has-error');
                            $('.edit-event__title').focus();
                        }
                    }

                    //Delete
                    if(calendarAction === 'delete') {
                        $('#edit-event').modal('hide');
                        setTimeout(function () {
                            swal({
                                title: 'Are you sure?',
                                text: "You won't be able to revert this!",
                                type: 'warning',
                                showCancelButton: true,
                                buttonsStyling: false,
                                confirmButtonClass: 'btn btn-danger',
                                confirmButtonText: 'Yes, delete it!',
                                cancelButtonClass: 'btn btn-secondary'
                            }).then(function() {
                                $('.calendar').fullCalendar('removeEvents', currentId);
                                swal({
                                    title: 'Deleted!',
                                    text: 'Your list has been deleted.',
                                    type: 'success',
                                    buttonsStyling: false,
                                    confirmButtonClass: 'btn btn-primary'
                                });
                            })
                        }, 200);
                    }
                });
*/

                //Calendar views switch
                $('body').on('click', '[data-calendar-view]', function(e){
                    e.preventDefault();

                    $('[data-calendar-view]').removeClass('actions__item--active');
                    $(this).addClass('actions__item--active');
                    var calendarView = $(this).attr('data-calendar-view');
                    $('.calendar').fullCalendar('changeView', calendarView);
                });


                //Calendar Next
                $('body').on('click', '.actions__calender-next', function (e) {
                    e.preventDefault();
                    $('.calendar').fullCalendar('next');
                });


                //Calendar Prev
                $('body').on('click', '.actions__calender-prev', function (e) {
                    e.preventDefault();
                    $('.calendar').fullCalendar('prev');
                });
                var i = moment().format("YYYY"),
                    j = moment().format("dddd, MMM D");
                $(".widget-calendar__year").html(i), $(".widget-calendar__day").html(j)
            });

});





