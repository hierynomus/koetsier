package nl.javadude.koetsier.snippet

import nl.javadude.koetsier.examen.{MultipleChoiceVraag, Examen, ExamenActor, HaalExamenOp}

class HelloWorld {
  def howdy = {
    val examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]

    <span>
      <h1>{examen.naam}</h1>
      <div> {
        for {
          val vraag <- examen.vragen
          <span><h2>{vraag.vraag}</h2></span>
          vraag match {
            case mc : MultipleChoiceVraag =>
          }
        }
      }</div>
    </span>
  }
}

