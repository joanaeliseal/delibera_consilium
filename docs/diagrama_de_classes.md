classDiagram
    class Aluno {
        -Long id
        -String nome
        -String fone
        -String matricula
        -String login
        -String senha
        -List~Processo~ processos
    }
    
    class Professor {
        -Long id
        -String nome
        -String fone
        -String matricula
        -String login
        -String senha
        -boolean coordenador
        -List~Processo~ processosRelator
        -List~Colegiado~ colegiados
        -List~Voto~ votos
    }
    
    class Colegiado {
        -Long id
        -LocalDate dataInicio
        -LocalDate dataFim
        -String descricao
        -String portaria
        -String curso
        -List~Professor~ membros
        -List~Reuniao~ reunioes
    }
    
    class Processo {
        -Long id
        -String numero
        -String textoRequerimento
        -LocalDate dataRecepcao
        -LocalDate dataDistribuicao
        -LocalDate dataParecer
        -byte[] parecer
        -String status
        -Aluno interessado
        -Assunto assunto
        -Professor relator
        -Reuniao reuniao
        -List~Voto~ votos
    }
    
    class Assunto {
        -Long id
        -String nome
    }
    
    class Reuniao {
        -Long id
        -LocalDate dataReuniao
        -StatusReuniao status
        -byte[] ata
        -Colegiado colegiado
        -List~Processo~ processosEmPauta
    }
    
    class Voto {
        -Long id
        -TipoVoto voto
        -boolean ausente
        -String justificativa
        -Processo processo
        -Professor professor
    }
    
    class StatusReuniao {
        <<enumeration>>
        PROGRAMADA
        ENCERRADA
    }
    
    class TipoVoto {
        <<enumeration>>
        COM_RELATOR
        DIVERGENTE
    }
    
    class TipoDecisao {
        <<enumeration>>
        DEFERIMENTO
        INDEFERIMENTO
    }
    
    %% Relacionamentos
    Aluno "1" --> "*" Processo : interessado
    Professor "1" --> "*" Processo : relator
    Professor "*" --> "*" Colegiado : membros
    Professor "1" --> "*" Voto : professor
    Processo "*" --> "1" Assunto : assunto
    Processo "*" --> "0..1" Reuniao : reuniao
    Processo "1" --> "*" Voto : processo
    Colegiado "1" --> "*" Reuniao : colegiado
    Reuniao --> StatusReuniao
    Voto --> TipoVoto
    Processo --> TipoDecisao