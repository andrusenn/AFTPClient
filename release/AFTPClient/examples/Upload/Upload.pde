import hidensource.aftp.*;




AFTPClient ftp;
String ruta = "";
void setup() {
      selectInput("Select a file to process:", "fileSelected");
      try {
            ftp = new AFTPClient(this, "host", 21, "user", "pass");
      }
      catch(Exception e) {
            //
      }
}
void draw() {
}
void mouseReleased() {
      try {
            if (ruta != "") {
                  ftp.uploadFile(ruta, "fotomaton");
            } else {
                  println("archivo vacio");
            }
      }
      catch(Exception e) {
            //
      }
      ftp.disconnect();
}
void fileSelected(File selection) {
      if (selection == null) {
            println("Window was closed or the user hit cancel.");
      } else {
            ruta = selection.getAbsolutePath();
      }
}