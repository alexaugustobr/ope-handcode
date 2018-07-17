
from django.db import models
from .Curso import Curso
from .Aluno import Aluno
from .Questao import Questao

class Resposta(models.Model):
    aluno = models.ForeignKey(to='Aluno', on_delete="cascade", related_name="respostas", null=False, blank=False) #onetomany
    questao = models.ForeignKey(to='Questao', on_delete="cascade",related_name="respostas", null=False, blank=False) #onetomany
    data_avaliacao = models.DateField(null=True,blank=True)
    nota = models.DecimalField(max_digits=4,decimal_places=2,null=True)
    avaliacao = models.TextField(null=True)
    descricao = models.TextField(null=False)
    data_de_envio = models.DateField(null=False)


    @property
    def data_formatada(self):
        return self.data.strftime("%Y-%m-%d")

    def __str__(self):
        return "{} - {}: {}".format(self.questao.id, self.aluno.ra, self.descricao)

    class Meta:
        db_table = 'Resposta'