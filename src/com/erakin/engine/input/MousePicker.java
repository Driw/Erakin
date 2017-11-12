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
 * <p>Através dessa classe é possível calcular e projetar um raio (linha/direção) no espaço conforme a posição do mouse.
 * Para isso será necessário a especificação da câmera que vai ser utilizada como também a matriz de projeção.
 * O cálculo é feito internamente quando for o objeto for chamado para atualizar, necessário a cada quadro processado.</p>
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
	 * Matriz de projeção usada na renderização.
	 */
	private Matrix4f projectionMatrix;

	/**
	 * Câmera usada para visualizar a tela.
	 */
	private Camera camera;

	/**
	 * Cria uma nova instância de um seletor de mouse sendo necessário definir:
	 * @param camera referência da câmera usada para visualizar a tela.
	 * @param projectionMatrix matriz de projeção usada na renderização.
	 */

	public MousePicker(Camera camera, Matrix4f projectionMatrix)
	{
		setCamera(camera);
		setProjectionMatrix(projectionMatrix);
	}

	/**
	 * @return aquisição da Câmera usada para visualizar a tela.
	 */

	public Camera getCamera()
	{
		return camera;
	}

	/**
	 * @param camera câmera usada para visualizar a tela.
	 */

	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	/**
	 * @return aquisição da matriz de projeção usada na renderização.
	 */

	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}

	/**
	 * @param projectionMatrix matriz de projeção usada na renderização.
	 */

	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}

	/**
	 * @return aquisição do posicionamento do raio projetado na tela.
	 */

	public Vector3f getRay()
	{
		return ray;
	}

	/**
	 * Realiza a atualização da projeção do raio no espaço conforme posicionamento do mouse na tela.
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
