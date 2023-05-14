
function RenderGame({game}) {

  return (
    <div>
        <table >
          <tbody>
            {game.map.map((row, rowIndex) => (
              <tr key={rowIndex}>
                {row.map((col, colIndex) => (
                  <td key={`${rowIndex}-${colIndex}`}>
                    {col}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
    </div>
  );
}

export default RenderGame;