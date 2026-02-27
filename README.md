# Java Calculator

Acest calculator poate efectua calcule pentru operatii mai complexe precum `2+4*(5-4*9)/234`, nu numai `2+2` sau `4*5`.

## Cum functioneaza

### 1. Interfata grafica
Calculatorul foloseste Java Swing pentru interfata grafica, cu butoane pentru cifre, operatori si paranteze.

### 2. toRPN
Transforma expresia normala (infix) in **Notatie Poloneza Inversa** (RPN).

Exemplu: `2*3+4` devine `2 3 * 4 +`

- Metoda parcurge expresia caracter cu caracter
- Numerele merg direct in output
- Operatorii sunt pusi pe o stiva temporara
- Cand gasim un operator cu prioritate mai mica, scoatem operatorii cu prioritate mai mare de pe stiva in output
- Parantezele controleaza ordinea operatiilor

### 3. evaluateRPN
Evalueaza expresia RPN si returneaza rezultatul.

- Parcurge token cu token expresia RPN
- Daca e numar → il pune pe stiva
- Daca e operator → scoate doua numere din stiva (`b` apoi `a`), calculeaza `a op b`, pune rezultatul inapoi pe stiva
- La final pe stiva ramane un singur numar — rezultatul final

## Exemplu

```
Intrare: 2*3+4*(4-3)
RPN: 23*443-*+
iesire: 10
```

P.S: M-am consultat cu diferite site-uri pentru a infrumuseta si sa fac fisier README.md mai lizibil
https://medium.com/design-bootcamp/how-to-design-an-attractive-github-profile-readme-3618d6c53783
https://github.com/banesullivan/README
