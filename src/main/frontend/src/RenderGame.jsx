function RenderGame(props) {

    return (
        <div>
            <table >
                <tbody>
                    {props.game.map.map((row, rowIndex) => (
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