# 🤝 Guia de Contribuição e Fluxo de Trabalho (CONTRIBUTING.md)

Bem-vindo(a) ao guia de contribuição do nosso projeto! Para mantermos o histórico limpo, rastreável e o código principal estável, pedimos que todos os colaboradores sigam as regras de Branching e Commit abaixo.

---

## 🌳 1. Padrão de Fluxo de Trabalho e Branching

O desenvolvimento segue o modelo de **Feature Branching**.

### A. Regra Fundamental: Uma Branch por Tarefa

Todo desenvolvimento (nova funcionalidade, correção de bug, ou refatoração) deve ser realizado em uma **Branch Isolada**.

> 🛑 **Atenção:** É estritamente proibido realizar commits diretamente na branch `main`. O trabalho deve ser sempre isolado em uma branch de feature e mesclado via Pull Request (PR).

### B. Nomenclatura das Branches

Use o seguinte padrão de prefixo para nomear as branches. Utilize letras minúsculas e separe as palavras por hífens (`-`).

| Prefixo | Significado | Exemplo |
| :--- | :--- | :--- |
| **`feat/`** | **New Feature:** Nova funcionalidade (Adicionar rota, nova API, etc.). | `feat/adiciona-calculo-frete` |
| **`fix/`** | **Bug Fix:** Correção de um comportamento incorreto. | `fix/corrige-erro-login-mobile` |
| **`chore/`** | **Chore:** Tarefa de manutenção, build, ou configuração (sem alteração lógica de negócio). | `chore/atualiza-dependencias-maven` |
| **`docs/`** | **Docs:** Alteração na documentação (README, comentários no código). | `docs/atualiza-readme-instalacao` |

### C. Fluxo de Criação da Branch

Antes de iniciar qualquer nova tarefa, garanta que sua cópia local da `main` esteja totalmente sincronizada:

```bash
# 1. Troca para a branch principal
git checkout main

# 2. Sincroniza com o repositório remoto
git pull origin main

# 3. Cria sua nova branch a partir da main atualizada
git checkout -b seu-novo-branch
```
## 💬 2. Regras de Mensagem dos Commits
As mensagens de commit devem seguir o Modo Imperativo (como se fosse um comando).
**Estrutura**: [tipo]: [verbo no imperativo] o que o commit faz

### Exemplo e Padrão Obrigatório
| Padrão **CORRETO** (Modo Imperativo) | Padrão a **EVITAR** (Passado ou Substantivo) |
| :--- | :--- |
| **`feat:`** Cria a classe Aluno. | Criada a classe Aluno. / Criação da classe Aluno. |
| **`fix:`** Corrige erro na validação de campos. | Corrigido o erro na validação. / Correção de validação. |
| **`chore:`** Adiciona script de inicialização. | Adicionei o script. / Inclusão de script. |

🚨 **Garantia de Qualidade**: Qualquer Pull Request com mensagens que não sigam o padrão será solicitado a ser corrigido antes da mesclagem (merge).
