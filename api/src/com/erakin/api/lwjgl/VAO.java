package com.erakin.api.lwjgl;

import static com.erakin.api.ErakinAPIUtil.sizeOfCollection;
import static com.erakin.api.lwjgl.GLUtil.glStore;
import static com.erakin.api.lwjgl.VBO.ARRAY_BUFFER;
import static com.erakin.api.lwjgl.VBO.ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.GL_MAX_VERTEX_ATTRIBS;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glIsVertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.Index;
import org.diverproject.util.collection.abstraction.StaticArray;
import org.lwjgl.opengl.GL15;

import com.erakin.api.ErakinRuntimeException;
import com.erakin.api.lwjgl.math.enumeration.DrawElement;

/**
 * <h1>Vertex-Array Objects</h1>
 *
 * <p>Tem como finalidade, a cria��o da ideia de um objecto como Vetor de V�rtice de Objetos.
 * Vetor por que aloca uma quantidade de objetos pr�-definido pelo mesmo no OpenGL.
 * V�rtice se referencia aos objetos que este ir� alocar, os objetos s�o os dados.</p>
 *
 * <p>VAO possui apenas um atributo, e este � de extrema import�ncia para funcionar.
 * A identifica��o do VAO permite vincular seu uso no OpenGl quando for usado,
 * como tamb�m desvincular ao parar de ser usado ou liberar este da mem�ria.</p>
 *
 * <p>Durante a cria��o de um VAO � utilizado <code>glGenVertexArrays</code>: ir� dizer ao
 * OpenGL para fazer uma alocagem de espa�o em mem�ria para armazenamento de dados.
 * Essa aloca��o ser� usada por VBOs quando assim for decidido por tal.</p>
 *
 * <p>O espa�o alocado � definido como um vetor enumerado de 16 posi��es, do 0 ao 15.
 * Essa enumera��o � denominada como Attribute, para cada um desses Attribute ser�
 * alocado um VBO que ir� determinar os dados respectivos desse VAO especificado.</p>
 *
 * @see GL15
 * @see GLBind
 *
 * @author Andrew
 */

public class VAO implements GLBind
{
	/**
	 * Quantidade limite de VBOs que podem ser criados por cada VAO.
	 */
	private static final int MAX_VBOS = glGetInteger(GL_MAX_VERTEX_ATTRIBS);

	/**
	 * Identifica��o do VAO no sistema OpenGL.
	 */
	private int id;

	/**
	 * Refer�ncia do VBO para armazenar os �ndices.
	 */
	private VBO indices;

	/**
	 * Quantidade de �ndices existentes nesse VAO.
	 */
	private int vertexCount;

	/**
	 * Refer�ncia de todos os VBO que podem ser criados.
	 */
	private Index<VBO> vbos;

	/**
	 * Constr�i um novo VAO, dizendo ao OpenGL para inicializar o mesmo.
	 * Essa inicializa��o ir� criar um novo VAO com atributos em branco.
	 */

	public VAO()
	{
		id = glGenVertexArrays();
		vbos = new StaticArray<VBO>(MAX_VBOS);
	}

	/**
	 * Constr�i um novo VAO, especificando qual sua identifica��o no OpenGL.
	 * Essa inicializa��o ir� criar um novo VAO com atributos em branco.
	 * @param vaoID identifica��o do VAO no OpenGL.
	 */

	public VAO(int vaoID)
	{
		if (vaoID < 0)
			throw new ErakinRuntimeException("id inv�lido (vaoID: %d)", vaoID);

		id = vaoID;
		vbos = new StaticArray<VBO>(MAX_VBOS);
	}

	/**
	 * Identifica��o do VAO permite dizer ao OpenGL quando ele deve ser usado.
	 * Tamb�m permite determinar quando parar de ser usado e remover da mem�ria.
	 * @return aquisi��o do c�digo para identifica��o desse VAO no OpenGL.
	 */

	public int getID()
	{
		return id;
	}

	/**
	 * Deve adicionar uma lista contendo o �ndice para identificar dados.
	 * Ir� determinar quantos dados de cada VBO em sequ�ncia forma um conjunto.
	 * Por exemplo, um vetor de inteiros definido como VBO [0, 1, 2, 3, 4, 6].
	 * @param data vetor contendo a indexa��o que ser� usada nos VBOs.
	 * @return aquisi��o da identifica��o do VBO dos �ndices.
	 */

	public int setIndices(int data[])
	{
		return setIndices(glStore(data));
	}

	/**
	 * Deve adicionar uma lista contendo o �ndice para identificar dados.
	 * Ir� determinar quantos dados de cada VBO em sequ�ncia forma um conjunto.
	 * Por exemplo, um vetor de inteiros definido como VBO [0, 1, 2, 3, 4, 6].
	 * @param buffer buffer contendo a indexa��o que ser� usada nos VBOs.
	 * @return aquisi��o da identifica��o do VBO dos �ndices.
	 */

	public int setIndices(IntBuffer buffer)
	{
		if (indices != null)
			indices.release();

		indices = new VBO(ELEMENT_ARRAY_BUFFER);
		indices.bind();
		indices.bufferData(buffer);
		// Por algum motivo esse VBO n�o deve dar unbind() igual aos atributos

		vertexCount = buffer.capacity();

		return indices.getID();
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que ser� usado ser� decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos ser� do tipo vetor de inteiros.
	 * @param index em qual �ndice ser� posicionado os dados (0 a 15).
	 * @param size quantas unidades cada �ndice definido no VAO ter�.
	 * @param data refer�ncia do vetor contendo os dados para armazenar.
	 * @return identifica��o do VBO no VAO, n�mero do atributo usado.
	 */

	public int setAttribute(int index, int size, int data[])
	{
		return setAttribute(index, size, glStore(data));
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que ser� usado ser� decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos ser� do tipo vetor de inteiros.
	 * @param index em qual �ndice ser� posicionado os dados (0 a 15).
	 * @param size quantas unidades cada �ndice definido no VAO ter�.
	 * @param buffer refer�ncia do buffer contendo os dados para armazenar.
	 * @return identifica��o do VBO no VAO, n�mero do atributo usado.
	 */

	public int setAttribute(int index, int size, IntBuffer buffer)
	{
		VBO vbo = vbos.get(index);

		if (vbo == null)
			vbo = new VBO(ARRAY_BUFFER);

		vbo.bind();
		vbo.bufferData(buffer);
		vbo.attribPointerInt(index, size);
		vbo.unbind();

		vbos.update(index, vbo);

		return vbo.getID();
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que ser� usado ser� decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos ser� do tipo vetor de flutuantes.
	 * @param index em qual �ndice ser� posicionado os dados (0 a 15).
	 * @param size quantas unidades cada �ndice definido no VAO ter�.
	 * @param data refer�ncia do vetor contendo os dados para armazenar.
	 * @return identifica��o do VBO no VAO, n�mero do atributo usado.
	 */

	public int setAttribute(int index, int size, float data[])
	{
		return setAttribute(index, size, glStore(data));
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que ser� usado ser� decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos ser� do tipo vetor de flutuantes.
	 * @param index em qual �ndice ser� posicionado os dados (0 a 15).
	 * @param size quantas unidades cada �ndice definido no VAO ter�.
	 * @param buffer refer�ncia do buffer contendo os dados para armazenar.
	 * @return identifica��o do VBO no VAO, n�mero do atributo usado.
	 */

	public int setAttribute(int index, int size, FloatBuffer buffer)
	{
		VBO vbo = vbos.get(index);

		if (vbo == null)
			vbo = new VBO(ARRAY_BUFFER);

		vbo.bind();
		vbo.bufferData(buffer);
		vbo.attribPointerFloat(index, size);
		vbo.unbind();

		vbos.update(index, vbo);

		return vbo.getID();
	}

	/**
	 * A contagem de v�rtices permite que um VAO seja usado para ser desenhado na tela na renderiza��o.
	 * O OpenGL n�o identifica quantos v�rtices existem nos �ndices ou qual atributo � dos v�rtices.
	 * @return aquisi��o da quantidade de v�rtices necess�rios para compor esse VAO.
	 */

	public int getVertexCount()
	{
		return vertexCount;
	}

	/**
	 * A contagem de v�rtices permite que um VAO seja usado para ser desenhado na tela na renderiza��o.
	 * O OpenGL n�o identifica quantos v�rtices existem nos �ndices ou qual atributo � dos v�rtices.
	 * @param vertexCount quantidade de v�rtices necess�rios para compor esse VAO.
	 */

	public void setVertexCount(int vertexCount)
	{
		this.vertexCount = vertexCount;
	}

	@Override
	public void release()
	{
		unbind();

		for (VBO vbo : vbos)
			if (vbo != null)
				vbo.release();

		if (indices != null)
			indices.release();

		glDeleteVertexArrays(id);

		indices = null;
		vbos = null;
		id = 0;
	}

	@Override
	public void bind()
	{
		glBindVertexArray(id);
	}

	@Override
	public void unbind()
	{
		glBindVertexArray(UNBIND_CODE);
	}

	@Override
	public boolean valid()
	{
		return id > 0 && glIsVertexArray(id);
	}

	/**
	 * Habilita um vetor de v�rtices associados com o �ndice especificado do atributo.
	 * Quando for habilitado significa que o OpenGL poder� us�-lo se for solicitado.
	 * @param attribute �ndice do atributo no VAO do qual dever� ser habilitado.
	 */

	public void enable(int attribute)
	{
		glEnableVertexAttribArray(attribute);
	}

	/**
	 * Desabilita um vetor de v�rtices associados com o �ndice especificado do atributo.
	 * Quando for desabilitado significa que o OpenGL n�o poder� us�-lo se solicitado.
	 * @param attribute �ndice do atributo no VAO do qual dever� ser desabilitado.
	 */

	public void disable(int attribute)
	{
		glDisableVertexAttribArray(attribute);
	}

	/**
	 * Define uma sequ�ncia geom�trica primitiva com todos os elementos armazenados em �ndice.
	 * @param mode em que modo ser� feito o desenho do VAO quando solicitado para renderizar.
	 * @see DrawElement
	 */

	public void draw(DrawElement mode)
	{
		if (indices != null)
			glDrawElements(mode.getValue(), getVertexCount(), GL_UNSIGNED_INT, 0);
		else
			glDrawArrays(mode.getValue(), 0, getVertexCount());
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("id", id);
		description.append("vbos", sizeOfCollection(vbos));
		description.append("vertexCount", vertexCount);

		return description.toString();
	}
}
