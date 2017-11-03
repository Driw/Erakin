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
 * <p>Tem como finalidade, a criação da ideia de um objecto como Vetor de Vértice de Objetos.
 * Vetor por que aloca uma quantidade de objetos pré-definido pelo mesmo no OpenGL.
 * Vértice se referencia aos objetos que este irá alocar, os objetos são os dados.</p>
 *
 * <p>VAO possui apenas um atributo, e este é de extrema importância para funcionar.
 * A identificação do VAO permite vincular seu uso no OpenGl quando for usado,
 * como também desvincular ao parar de ser usado ou liberar este da memória.</p>
 *
 * <p>Durante a criação de um VAO é utilizado <code>glGenVertexArrays</code>: irá dizer ao
 * OpenGL para fazer uma alocagem de espaço em memória para armazenamento de dados.
 * Essa alocação será usada por VBOs quando assim for decidido por tal.</p>
 *
 * <p>O espaço alocado é definido como um vetor enumerado de 16 posições, do 0 ao 15.
 * Essa enumeração é denominada como Attribute, para cada um desses Attribute será
 * alocado um VBO que irá determinar os dados respectivos desse VAO especificado.</p>
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
	 * Identificação do VAO no sistema OpenGL.
	 */
	private int id;

	/**
	 * Referência do VBO para armazenar os índices.
	 */
	private VBO indices;

	/**
	 * Quantidade de índices existentes nesse VAO.
	 */
	private int vertexCount;

	/**
	 * Referência de todos os VBO que podem ser criados.
	 */
	private Index<VBO> vbos;

	/**
	 * Constrói um novo VAO, dizendo ao OpenGL para inicializar o mesmo.
	 * Essa inicialização irá criar um novo VAO com atributos em branco.
	 */

	public VAO()
	{
		id = glGenVertexArrays();
		vbos = new StaticArray<VBO>(MAX_VBOS);
	}

	/**
	 * Constrói um novo VAO, especificando qual sua identificação no OpenGL.
	 * Essa inicialização irá criar um novo VAO com atributos em branco.
	 * @param vaoID identificação do VAO no OpenGL.
	 */

	public VAO(int vaoID)
	{
		if (vaoID < 0)
			throw new ErakinRuntimeException("id inválido (vaoID: %d)", vaoID);

		id = vaoID;
		vbos = new StaticArray<VBO>(MAX_VBOS);
	}

	/**
	 * Identificação do VAO permite dizer ao OpenGL quando ele deve ser usado.
	 * Também permite determinar quando parar de ser usado e remover da memória.
	 * @return aquisição do código para identificação desse VAO no OpenGL.
	 */

	public int getID()
	{
		return id;
	}

	/**
	 * Deve adicionar uma lista contendo o índice para identificar dados.
	 * Irá determinar quantos dados de cada VBO em sequência forma um conjunto.
	 * Por exemplo, um vetor de inteiros definido como VBO [0, 1, 2, 3, 4, 6].
	 * @param data vetor contendo a indexação que será usada nos VBOs.
	 * @return aquisição da identificação do VBO dos índices.
	 */

	public int setIndices(int data[])
	{
		return setIndices(glStore(data));
	}

	/**
	 * Deve adicionar uma lista contendo o índice para identificar dados.
	 * Irá determinar quantos dados de cada VBO em sequência forma um conjunto.
	 * Por exemplo, um vetor de inteiros definido como VBO [0, 1, 2, 3, 4, 6].
	 * @param buffer buffer contendo a indexação que será usada nos VBOs.
	 * @return aquisição da identificação do VBO dos índices.
	 */

	public int setIndices(IntBuffer buffer)
	{
		if (indices != null)
			indices.release();

		indices = new VBO(ELEMENT_ARRAY_BUFFER);
		indices.bind();
		indices.bufferData(buffer);
		// Por algum motivo esse VBO não deve dar unbind() igual aos atributos

		vertexCount = buffer.capacity();

		return indices.getID();
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que será usado será decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos será do tipo vetor de inteiros.
	 * @param index em qual índice será posicionado os dados (0 a 15).
	 * @param size quantas unidades cada índice definido no VAO terá.
	 * @param data referência do vetor contendo os dados para armazenar.
	 * @return identificação do VBO no VAO, número do atributo usado.
	 */

	public int setAttribute(int index, int size, int data[])
	{
		return setAttribute(index, size, glStore(data));
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que será usado será decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos será do tipo vetor de inteiros.
	 * @param index em qual índice será posicionado os dados (0 a 15).
	 * @param size quantas unidades cada índice definido no VAO terá.
	 * @param buffer referência do buffer contendo os dados para armazenar.
	 * @return identificação do VBO no VAO, número do atributo usado.
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
	 * O atributo que será usado será decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos será do tipo vetor de flutuantes.
	 * @param index em qual índice será posicionado os dados (0 a 15).
	 * @param size quantas unidades cada índice definido no VAO terá.
	 * @param data referência do vetor contendo os dados para armazenar.
	 * @return identificação do VBO no VAO, número do atributo usado.
	 */

	public int setAttribute(int index, int size, float data[])
	{
		return setAttribute(index, size, glStore(data));
	}

	/**
	 * Deve adicionar uma nova quantidade de dados para um atributo do VAO.
	 * O atributo que será usado será decidido automaticamente pelo OpenGL.
	 * Para esse caso, os dados definidos será do tipo vetor de flutuantes.
	 * @param index em qual índice será posicionado os dados (0 a 15).
	 * @param size quantas unidades cada índice definido no VAO terá.
	 * @param buffer referência do buffer contendo os dados para armazenar.
	 * @return identificação do VBO no VAO, número do atributo usado.
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
	 * A contagem de vértices permite que um VAO seja usado para ser desenhado na tela na renderização.
	 * O OpenGL não identifica quantos vértices existem nos índices ou qual atributo é dos vértices.
	 * @return aquisição da quantidade de vértices necessários para compor esse VAO.
	 */

	public int getVertexCount()
	{
		return vertexCount;
	}

	/**
	 * A contagem de vértices permite que um VAO seja usado para ser desenhado na tela na renderização.
	 * O OpenGL não identifica quantos vértices existem nos índices ou qual atributo é dos vértices.
	 * @param vertexCount quantidade de vértices necessários para compor esse VAO.
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
	 * Habilita um vetor de vértices associados com o índice especificado do atributo.
	 * Quando for habilitado significa que o OpenGL poderá usá-lo se for solicitado.
	 * @param attribute índice do atributo no VAO do qual deverá ser habilitado.
	 */

	public void enable(int attribute)
	{
		glEnableVertexAttribArray(attribute);
	}

	/**
	 * Desabilita um vetor de vértices associados com o índice especificado do atributo.
	 * Quando for desabilitado significa que o OpenGL não poderá usá-lo se solicitado.
	 * @param attribute índice do atributo no VAO do qual deverá ser desabilitado.
	 */

	public void disable(int attribute)
	{
		glDisableVertexAttribArray(attribute);
	}

	/**
	 * Define uma sequência geométrica primitiva com todos os elementos armazenados em índice.
	 * @param mode em que modo será feito o desenho do VAO quando solicitado para renderizar.
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
