library(lubridate)

getData <- function () {
  download.file(url="https://docs.google.com/spreadsheets/d/1qyhjFF4CcjeSu__88_WOLjFumuguDmVWTHbOlrikEvI/pub?gid=0&single=true&output=csv",
                   destfile="running-log.csv")
}

createDataFrames <- function() {
  
  rawdf <- read.csv("running-log.csv")
  runningdf <- rawdf[rawdf$Type == "Running" & rawdf$Pace != "00:00", ]
  cyclingdf <- rawdf[rawdf$Type == "Cycling", ]
  
  runningPace <- runningdf[, c("Pace")]
  View(runningPace)
  View(period_to_seconds(ms(runningPace)))
  runningPaceTs <- ts(period_to_seconds(ms(runningPace)))
  plot.ts(runningPaceTs)
}

getData()
createDataFrames()