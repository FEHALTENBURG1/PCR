
# 🫀 PCR Assist — Adulto (UnB/HUB)

Painel web de apoio à condução da **reanimação cardiopulmonar (RCP) em adultos** durante uma parada cardiorrespiratória (PCR). Versão em **Java puro** (sem dependências externas, apenas o JDK) + **HTML/CSS/JS**, com a mesma lógica e identidade visual da versão em R Shiny.

> ⚠️ **Aviso**: ferramenta de apoio educacional e de treinamento. Não substitui protocolos institucionais, as diretrizes vigentes (AHA/ERC) nem o julgamento clínico da equipe. O log não contém dados pessoais.

## Funcionalidades

- ⏱ **Cronômetro** com iniciar/pausar/retomar e zerar (com confirmação)
- 📊 **KPIs**: choques, estado, horário de início das compressões e ritmo atual
- 〰 **Registro de ritmo**: FV, TVSP, AESP e Assistolia — com orientação contextual (chocável × não chocável, 5H/5T)
- 🔁 **Ciclo de 2 min** com contagem regressiva para reavaliação de ritmo
- 💉 **Alertas escalonados de epinefrina** (preparar → pré-alerta → alerta forte → agora → atraso ≥ 05:00) e, após a 1ª dose, lembrete de repetição a cada 3–5 min
- ⚡ Aviso de **ritmo não chocável** ao registrar choque em AESP/Assistolia
- 💊 **Hall de medicações** da PCR (epinefrina, amiodarona, lidocaína, sulfato de Mg, bicarbonato, cálcio, naloxona, fibrinolítico, volume, glicose) com doses de referência — cada clique registrado com timestamp duplo (horário real + tempo relativo)
- 📋 **Log de eventos** (Momento, T_rel, Evento) com **exportação CSV** e sincronização com o backend Java
- 📝 **Texto de evolução** gerado automaticamente (narrativa + linha do tempo completa) com botões de **copiar** e **baixar .txt** para colar no prontuário
- ⌨️ **Atalhos**: Espaço = Iniciar/Pausar • C = Início • E = Epinefrina • J = Choque • R = RCE • F = FV • T = TVSP • P = AESP • A = Assistolia
- ⛶ Modo **tela cheia**, tema claro com paleta institucional UnB/HUB + EBSERH, foco visível e suporte a `prefers-reduced-motion`

## Logomarca (opcional)

Coloque o arquivo `brand_unb_hub_ebserh.png` em `src/main/resources/static/` para exibi-lo na barra superior. Sem o arquivo, o app funciona normalmente.

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

Abra **http://localhost:8080** no navegador. Para usar outra porta:

```bash
java -cp out:src/main/resources br.pcr.Main 9090
```

> 💡 A página também funciona de forma independente: basta abrir `src/main/resources/static/index.html` direto no navegador (o backend só é necessário para persistir o log via API).

## API

| Método | Rota           | Descrição                          |
|--------|----------------|------------------------------------|
| GET    | `/api/eventos` | Lista os eventos registrados (JSON)|
| POST   | `/api/eventos` | Registra um evento (corpo = texto) |
| DELETE | `/api/eventos` | Limpa o registro                   |

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
