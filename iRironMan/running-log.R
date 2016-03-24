getData <- function () {
  download.file(url="https://docs.google.com/spreadsheets/d/1qyhjFF4CcjeSu__88_WOLjFumuguDmVWTHbOlrikEvI/pub?gid=0&single=true&output=csv",
                   destfile="/home/dev/R/running-log.csv")
  
  
}

rawdf <- read.csv("/home/dev/R/running-log.csv")
