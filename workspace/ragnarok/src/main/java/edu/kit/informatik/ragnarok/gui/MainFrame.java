package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainFrame {
/**
    * <pre>
    *           1..1     1..1
    * MainFrame ------------------------> GameView
    *           mainFrame        &gt;       gameView
    * </pre>
    */
   private GameView gameView;
   
   public void setGameView(GameView value) {
      this.gameView = value;
   }
   
   public GameView getGameView() {
      return this.gameView;
   }
   

	protected Shell shell;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		this.createContents();
		this.shell.open();
		this.shell.layout();
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		this.shell = new Shell(SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL);
		this.shell.setSize(450, 300);

		this.shell.setText("Pr(o)ject - Ragnarok");
		this.shell.setLayout(new GridLayout(1, false));

		Canvas canvas = new Canvas(this.shell, SWT.NONE);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}
}
