#--------------------------------------------------------------------------
# Gera log de acesso
#--------------------------------------------------------------------------
function fsega200_logAcesso(servico,inout)
    define servico char(200)
    define inout   INTEGER
    
    if inout = 0 THEN   
       LET command = "logger -t ACESSO -p local3.info 'O usuario ",  
                      g_issk.usrcod, " entrou no servico: ", servico
    else 
       LET command = "logger -t ACESSO -p local3.info 'O usuario ",  
                      g_issk.usrcod, " saiu do servico: ", servico
    end if
            
    RUN command RETURNING retVal
    IF retVal = 0 THEN
       let m_msgErro = "Looger excecutado com sucesso."
       call errorlog(m_msgErro)
    ELSE
       let m_msgErro = "Erro ao executar Looger."
       call errorlog(m_msgErro)
    END IF
         
#--------------------------------------------------------------------------
 end function
#