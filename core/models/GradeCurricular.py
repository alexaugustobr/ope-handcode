
from django.db import models
from .Curso import Curso

class GradeCurricular(models.Model):
    curso = models.ForeignKey(to='Curso', on_delete="cascade", related_name="gradesCurriculares", null=False, blank=False) #onetomany
    ano = models.SmallIntegerField(null=False)
    semestre = models.CharField(max_length=1,null=False)
    
    def __str__(self):
        return "{}: {} - {}".format(self.curso, self.ano, self.semestre)
    class Meta:
        db_table = 'GradeCurricular'
       