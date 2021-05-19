package parte2;

import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import parte2.Mensajes.*;

public class Menu extends JFrame {
	JLabel lblTexto;
	JComboBox<String> lstLista;
	ObjectOutputStream foutc;
	ObjectInputStream finc;
	String nombre_usuario;

	public Menu(ObjectInputStream finc, ObjectOutputStream foutc, String nombre_usuario) {
		super("Menu " + nombre_usuario);
		this.finc = finc;
		this.foutc = foutc;
		this.nombre_usuario = nombre_usuario;

		// lock.releaseLock(0);
		this.setSize(500, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		showMenu();

	}

	public void showMenu() {
		String[] valores = { "1", "2", "3" };
		lstLista = new JComboBox<String>(valores);

		/*
		 * lstLista.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * //System.out.println("has seleccionado: " + (String)
		 * lstLista.getSelectedItem()); //lblTexto.setText((String)
		 * lstLista.getSelectedItem()); } });
		 */
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switch ((String) lstLista.getSelectedItem()) {
					case "1":
						System.out.println("Ha seleccionado: lista de usuarios");
						// Enviar mensaje MSG_LISTA_USUARIOS
						try {
							foutc.writeObject(new Msg_lista_usuarios(nombre_usuario, "Servidor"));
							foutc.flush();
						} catch (IOException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						// lock.takeLock(0);
						// System.out.println("PRUEBA : Cliente - lista");
						// lock.releaseLock(0);
						break;
					case "2":
						System.out.println("Ha seleccionado: descargar fichero");
						String path = JOptionPane.showInputDialog("Escriba el nombre del fichero:");
						System.out.println("El fichero que quiere es: " + path);
						// (se deben permitir otras acciones y descargas simultaneas)
						// Enviar mensaje MSG_SOLICITUD_FICHERO
						// lock.takeLock(0);
						// System.out.println("Indica el nombre del fichero que quieres descargar: ");
						// String nombre_fichero = teclado.nextLine();
						// lock.releaseLock(0);

						try {
							foutc.writeObject(new Msg_solicitud_fichero(path,nombre_usuario,"Servidor"));
							foutc.flush();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						break;
					case "3":
						System.out.println("Ha seleccionado: salir");
						// Enviar mensaje MSG_CERRAR_CONEXION
						try {
							foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario,"Servidor"));
							foutc.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Menu.this.dispose();
						System.exit(0);
						// ok = false; //Cerramos este hilo y el hilo OS se queda esperando confirmación
						break;
					default:
						// lock.takeLock(0);
						System.out.println("Opcion no valida");
						// lock.releaseLock(0);
						break;
				}
			}
		});

		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Enviar mensaje MSG_CERRAR_CONEXION
				try {
					foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario,"Servidor"));
					foutc.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Menu.this.dispose();
				System.exit(0);
			}
		});

		JPanel pnl = new JPanel();

		pnl.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		pnl.setLayout(new GridLayout(4, 2, 5, 5));

		pnl.add(new JLabel("1 - Consultar el nombre de los usuarios conectados y su informacion\n"));
		pnl.add(new JLabel(""));
		pnl.add(new JLabel("2 - Descargar informacion\n"));
		pnl.add(new JLabel(""));
		pnl.add(new JLabel("3 - Desconectarse del servidor\n"));
		pnl.add(lstLista);
		pnl.add(btnSalir);
		pnl.add(btnAceptar);
		this.getContentPane().add(pnl);
		this.pack();

	}
	/*
	 * public static void main(String[] args) { // Schedule a job for the
	 * event-dispatching thread:
	 * 
	 * javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * Menu ej = new Menu();
	 * 
	 * } }); }
	 */
}
