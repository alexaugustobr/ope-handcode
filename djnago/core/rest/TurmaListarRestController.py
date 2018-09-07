from django.http import JsonResponse
import json
from core.models import Turma

def turmaListarJson(request):

    turmas = []

    if 'cursoId' in request.GET.keys():
        turmas = Turma.objects.filter(curso=request.GET['cursoId'])

    contexto = {}

    contexto['turmas'] = []

    for turma in turmas:
        contexto['turmas'].append(turma.toJson())

    return JsonResponse(contexto)