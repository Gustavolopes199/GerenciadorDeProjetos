# Gerenciador de Projetos

* Configuração mock-webclient: https://www.baeldung.com/spring-mocking-webclient
* Implementado @DinamicPropertySource para confirar realizar o teste com WebMockServer
    * Configurado para quando o teste rodar, injetar o baseUrl no webClient e conseguir fazer a requisição no endereço correto


## Rules 
  * Iria utilizar Map na manutenção de status do projeto, mas a implementação fica muito complexa para manutenção, decidi usar vários ifs
    # Risco Projeto - 👌
    *  Baixo risco: orçamento até R$ 100.000 e prazo ≤ 3 meses
    *  Médio risco: orçamento entre R$ 100.001 e R$ 500.000 ou prazo entre 3 a 6 meses
    *  Alto risco: orçamento acima de R$ 500.000 ou prazo superior a 6 meses
    # Status Projeto - 👌
    * Status Ordem: em análise → análise realizada → análise aprovada → iniciado → planejado → em andamento → encerrado
    * Exceções:
      * Cancelado -> Aplicado a qualquer momento
      * Exclusão -> Somente se não estiver em *Iniciado*, *Em Andamento* ou *Encerrado*
    # Membros - Projeto - 👌
    * Alocar no projeto 1 Gerente
    * Minimo 1 Máximo 10
    * Membro não pode ter mais de 3 projetos cujo status seja diferente de encerrado ou cancelado;
# EndPoints
  - Utilizar Spring Security -> Liberar cors para front-end/Req externa
  * ## Projetos 
    * Busca Paginada + Filtros -> Documentar com swagger 
      * Ref: Projeto do integrador com aprovação de produto, query que buscava 90 mil produtos com filtros
  * ## Relatorios
    * Quantidade de Projetos por status
    * Total Orçado Por Status
    * Media de dureação dos projetos encerrados
    * Total de Membros unicos alocados