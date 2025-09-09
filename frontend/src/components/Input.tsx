interface InputProps {
  type: string;
  id: string;
  name: string;
  placeholder: string;
}

export default function Input({ id, name, placeholder, type }: InputProps) {
    
  return (
    <input
      type={type}
      id={id}
      name={name}
      className="w-full px-3 py-2 lg:px-4 lg:py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-accent focus:border-transparent"
      placeholder={placeholder}
    />
  );
}
