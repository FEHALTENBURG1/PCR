# PCR Assist — Adulto (UnB/SDS)

Painel web de apoio à condução da **reanimação cardiopulmonar (RCP) em adultos** durante uma parada cardiorrespiratória (PCR). Backend em **Java puro** (apenas o JDK, sem dependências externas) e frontend em **HTML/CSS/JS**, com identidade visual institucional, ícones em outline e interface sóbria voltada ao uso clínico.

> **Aviso:** ferramenta de apoio educacional e de treinamento. Não substitui protocolos institucionais, as diretrizes vigentes (AHA/ERC) nem o julgamento clínico da equipe. O registro não contém dados pessoais.

## Funcionalidades

- **Cronômetro** com iniciar/pausar/retomar e zerar (com confirmação)
- **KPIs**: choques, estado, horário de início das compressões e ritmo atual
- **Registro de ritmo**: FV, TVSP, AESP e Assistolia, com orientação contextual (chocável × não chocável, 5H/5T)
- **Ciclo de 2 min** com contagem regressiva para reavaliação de ritmo
- **Metrônomo de compressões** ajustável (90–130/min), com indicador visual e clique sonoro; liga junto com o cronômetro
- **Avisos sonoros automáticos**: fim de cada ciclo de 2 min e escalonamento do alerta de epinefrina (pré-alerta, alerta, agora, atraso). Botão para ligar/desligar todos os sons
- **Alertas de epinefrina** escalonados por tempo (< 05:00) e lembrete de repetição a cada 3–5 min após a 1ª dose
- **Hall de medicações** da PCR (epinefrina, amiodarona, lidocaína, sulfato de magnésio, bicarbonato, cálcio, naloxona, fibrinolítico, volume, glicose) com doses de referência ACLS — cada clique registrado com timestamp duplo (horário real + tempo relativo)
- **Registro de eventos** (Momento, T relativo, Evento) com exportação **CSV** e sincronização com o backend Java
- **Texto de evolução** gerado automaticamente (narrativa + linha do tempo completa) com botões de **copiar** e **baixar .txt** para o prontuário
- **Atalhos de teclado**, modo **tela cheia**, foco visível e suporte a `prefers-reduced-motion`

## Atalhos

| Tecla | Ação | Tecla | Ação |
|-------|------|-------|------|
| Espaço | Iniciar/Pausar | F | FV |
| C | Início de massagem | T | TVSP |
| E | Epinefrina | P | AESP |
| J | Choque | A | Assistolia |
| R | RCE | M | Metrônomo |
| S | Sons on/off | | |

## Logomarca (opcional)

Coloque `brand_unb_hub_ebserh.png` em `src/main/resources/static/` para exibi-la no cabeçalho. Sem o arquivo, um ícone padrão é usado automaticamente.

## Requisitos

- Java 11 ou superior (apenas o JDK — nenhuma biblioteca externa)

## Como rodar

```bash
# Compilar
javac -d out src/main/java/br/pcr/Main.java

# Executar (Linux/macOS)
java -cp out:src/main/resources br.pcr.Main

# Executar (Windows)
java -cp out;src/main/resources br.pcr.Main
```

Abra **http://localhost:8080**. Para usar outra porta: `java -cp out:src/main/resources br.pcr.Main 9090`.

> O painel também funciona de forma independente: basta abrir `src/main/resources/static/index.html` no navegador (o backend só é necessário para persistir o log via API). Os sons exigem uma interação inicial na página (clique/tecla), por exigência dos navegadores.

## API

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/eventos` | Lista os eventos registrados (JSON) |
| POST | `/api/eventos` | Registra um evento (corpo = texto) |
| DELETE | `/api/eventos` | Limpa o registro |

## Estrutura

```
pcr-assist/
├── README.md
├── .gitignore
└── src/main/
    ├── java/br/pcr/Main.java          # Servidor HTTP (JDK puro)
    └── resources/static/index.html    # Painel web (frontend completo)
```

## Licença

MIT — use, modifique e compartilhe livremente.

## Autoria

Desenvolvido por **Enfermeira Fernanda Haltenburg**.
