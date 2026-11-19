package it.unicam.cs.mpgc.rpg126615.view.corridoio;

record CorsaEroe(double daX, double daY, double aX, double aY,
                 double frazioneVisibile, double orientamento) {

    double spostamentoX(double frazione) {
        return this.daX + (this.aX - this.daX) * frazione;
    }

    double spostamentoY(double frazione) {
        return this.daY + (this.aY - this.daY) * frazione;
    }

    double opacita(double frazione) {
        if (isUscita()) {
            double svanisce = (frazione - this.frazioneVisibile) / (1 - this.frazioneVisibile);
            return 1 - limita(svanisce);
        }
        return limita(frazione / this.frazioneVisibile);
    }

    private boolean isUscita() {
        return this.aX != 0 || this.aY != 0;
    }

    private static double limita(double valore) {
        return Math.max(0, Math.min(1, valore));
    }
}