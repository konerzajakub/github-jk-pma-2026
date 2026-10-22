# Fragmenty

Jedna aktivita se dvěma tlačítky. Každé tlačítko zobrazí v kontejneru `FragmentContainerView` jiný prázdný fragment (`FragmentA`, `FragmentB`).

![Fragment A](screenshots/screenshot-fragment-a.png) ![Fragment B](screenshots/screenshot-fragment-b.png)

Přepnutí dělá `supportFragmentManager.beginTransaction().replace(...).commit()` v `MainActivity`.
