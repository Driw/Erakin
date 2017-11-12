package com.erakin.engine.input;

import static com.erakin.engine.ErakinMaths.createViewMatrix;
import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.erakin.engine.camera.Camera;

/**
 * <h1>Seletor de Mouse</h1>
 *
 * <p>Atrav�s dessa classe � poss�vel calcular e projetar um raio (linha/dire��o) no espa�o conforme a posi��o do mouse.
 * Para isso ser� necess�rio a especifica��o da c�mera que vai ser utilizada como tamb�m a matriz de proje��o.
 * O c�lculo � feito internamente quando for o objeto for chamado para atualizar, necess�rio a cada quadro processado.</p>
 *
 * @see Camera
 * @see Vector3f
 * @see Matrix4f
 *
 * @author Andrew
 */

public class MousePicker
{
	/**
	 * Vetor de posicionamento do raio projetado na tela.
	 */
	private Vector3f ray;

	/**
	 * Matriz de proje��o usada na renderiza��o.
	 */
	private Matrix4f projectionMatrix;

	/**
	 * C�mera usada para visualizar a tela.
	 */
	private Camera camera;

	/**
	 * Cria uma nova inst�ncia de um seletor de mouse sendo necess�rio definir:
	 * @param camera refer�ncia da c�mera usada para visualizar a tela.
	 * @param projectionMatrix matriz de proje��o usada na renderiza��o.
	 */

	public MousePicker(Camera camera, Matrix4f projectionMatrix)
	{
		setCamera(camera);
		setProjectionMatrix(projectionMatrix);
	}

	/**
	 * @return aquisi��o da C�mera usada para visualizar a tela.
	 */

	public Camera getCamera()
	{
		return camera;
	}

	/**
	 * @param camera c�mera usada para visualizar a tela.
	 */

	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	/**
	 * @return aquisi��o da matriz de proje��o usada na renderiza��o.
	 */

	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}

	/**
	 * @param projectionMatrix matriz de proje��o usada na renderiza��o.
	 */

	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}

	/**
	 * @return aquisi��o do posicionamento do raio projetado na tela.
	 */

	public Vector3f getRay()
	{
		return ray;
	}

	/**
	 * Realiza a atualiza��o da proje��o do raio no espa�o conforme posicionamento do mouse na tela.
	 */

	public void update()
	{
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();

		Vector2f normalizedDevice = getNormalizedDevice(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedDevice.x, normalizedDevice.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		ray = toWorldCoords(eyeCoords);
	}

	/**
	 * TODO
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */

	private Vector2f getNormalizedDevice(float mouseX, float mouseY)
	{
		float x = (2f * mouseX) / Display.getWidth() - 1f;
		float y = (2f * mouseY) / Display.getHeight() - 1f;

		return new Vector2f(x, y);
	}

	/**
	 * TODO
	 * @param clipCoords
	 * @return
	 */

	private Vector4f toEyeCoords(Vector4f clipCoords)
	{
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);

		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	/**
	 * TODO
	 * @param eyeCoords
	 * @return
	 */

	private Vector3f toWorldCoords(Vector4f eyeCoords)
	{
		Matrix4f invertedView = Matrix4f.invert(createViewMatrix(camera), null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();

		return mouseRay;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("ray", getRay());
		description.append("camera", nameOf(getCamera()));

		return description.toString();
	}
}
