package mx.edu.ittepic.ladm_u3_practica2_campos_aguirre_lilian_michel

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        pedidoProducto.setOnClickListener {
            producto.isEnabled = pedidoProducto.isChecked
            precio.isEnabled = pedidoProducto.isChecked
            cantidad.isEnabled = pedidoProducto.isChecked
            entregado.isEnabled = pedidoProducto.isChecked
        }
        pedidoDescripcion.setOnClickListener {
            descripcion.isEnabled = pedidoDescripcion.isChecked
            precio.isEnabled = pedidoDescripcion.isChecked
            cantidad.isEnabled = pedidoDescripcion.isChecked
            entregado.isEnabled = pedidoDescripcion.isChecked
        }

        btnInsertar.setOnClickListener {
            insertar()
        }

        btnConsulta.setOnClickListener {
            construirDialogo()
        }

    }
    private fun construirDialogo(){
        var dialogo = Dialog(this)
        dialogo.setContentView(R.layout.consulta)//Todos los objetos de consulta se cargan en dialogo

        //Declaracion de objetos de consulta
        var valor = dialogo.findViewById<EditText>(R.id.valor)
        var posicion = dialogo.findViewById<Spinner>(R.id.clave)
        var buscar = dialogo.findViewById<Button>(R.id.buscar)
        var cerrar = dialogo.findViewById<Button>(R.id.cerrar)
        dialogo.show()
        cerrar.setOnClickListener {
            dialogo.dismiss()//Se cierra el dialogo
        }
        buscar.setOnClickListener {
            if (valor.text.isEmpty()){
                Toast.makeText(this, "DEBES PONER VALOR PARA BUSCAR", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            realizarConsulta(valor.text.toString(), posicion.selectedItemPosition)
            dialogo.dismiss()
        }
    }
    private  fun realizarConsulta(valor:String, clave:Int){
//CLAVE: 0 Nombre, 1 Domicilio, 2 Producto
        when(clave){
            0->{consultaNombre(valor)}
            1->{consultaDomicilio(valor)}
        }
    }

    private fun consultaNombre(valor: String){
        baseRemota.collection("restaurante")
            .whereEqualTo("nombre",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null ){
                    resultado.setText("ERROR NO HAY CONEXION")
                    return@addSnapshotListener
                }
                var res = ""
                for(document in querySnapshot!!){
                    res += "\nID"+document.id+"\nNombre: "+document.getString("nombre")+
                    "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nProducto: "+document.get("pedido.producto")+
                            "\nDescripcion: "+document.get("pedido.descripcion")+
                    "\nPrecio: "+document.get("pedido.precio")+
                            "\nCantidad: "+document.get("pedido.cantidad")+
                            "\nEntregado: "+document.get("pedido.entregado")+"\n"
                }
                resultado.setText(res)
            }
    }

    private fun consultaDomicilio(valor: String){
        baseRemota.collection("restaurante")
            .whereEqualTo("domicilio",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null ){
                    resultado.setText("ERROR NO HAY CONEXION")
                    return@addSnapshotListener
                }
                var res = ""
                for(document in querySnapshot!!){
                    res += "\nID"+document.id+"\nNombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nProducto: "+document.get("pedido.producto")+
                            "\nDescripcion: "+document.get("pedido.descripcion")+
                            "\nPrecio: "+document.get("pedido.precio")+
                            "\nCantidad: "+document.get("pedido.cantidad")+
                            "\nEntregado: "+document.get("pedido.entregado")+"\n"
                }
                resultado.setText(res)
            }
    }

    private fun insertar(){
        /*
        NOMBRE CADENA
        DOMICILIO CADENA
        CELULAR CADENA
        DESCRIPCION/PRODUCTO CADENA
        PRECIO DECIMAL
        CANTIDAD ENTERO
        ENTREGADO BOOLEANO
        */
        var datosInsertar = hashMapOf(
            "nombre" to nombre.text.toString(),
            "domicilio" to domicilio.text.toString(),
            "celular" to celular.text.toString(),
            "pedido" to hashMapOf(
                "descripcion" to descripcion.text.toString(),
                "precio" to precio.text.toString().toDouble(),
                "cantidad" to cantidad.text.toString().toInt(),
                "entregado" to entregado.text.toString().toBoolean()
            )
        )
        baseRemota.collection("restaurante")
            .add(datosInsertar)
            .addOnSuccessListener{
                Toast.makeText(this, "SE CAPTURÓ", Toast.LENGTH_LONG)
                    .show()
                limpiarCampos()
            }
            .addOnFailureListener {
                Toast.makeText(this, "ATENCION\n NO SE LOGRO CAPTURAR", Toast.LENGTH_LONG)
                    .show()
            }

    }//Insertar

    private fun limpiarCampos(){
        nombre.setText("")
        domicilio.setText("")
        celular.setText("")
        producto.setText("")
        descripcion.setText("")
        precio.setText("")
        cantidad.setText("")
        entregado.setText("")
    }
}



